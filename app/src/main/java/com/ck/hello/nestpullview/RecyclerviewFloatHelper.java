package com.ck.hello.nestpullview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

/**
 * Created by 不听话的好孩子 on 2018/4/27.
 */

public class RecyclerviewFloatHelper {
    FloatInterface floatInterface;

    public RecyclerviewFloatHelper(FloatInterface floatInterface) {
        this.floatInterface = floatInterface;
    }

    public void setOnFloatClickListener(OnFloatClickListener listener) {
        floatInterface.setOnFloatClickListener(listener);
    }

    public void attachRecyclerview(RecyclerView recyclerView) {
        floatInterface.attachRecyclerview(recyclerView);
    }

    public interface FloatInterface {

        void attachRecyclerview(RecyclerView recyclerView);

        void setOnFloatClickListener(OnFloatClickListener listener);
    }

    public interface OnFloatClickListener {
        void onClick(View v, int position);
    }

    public static class ViewTypeFloatView extends RecyclerView.OnScrollListener implements RecyclerviewFloatHelper.FloatInterface {
        RecyclerView.Adapter adapter;
        OnFloatClickListener listener;
        ViewGroup container;
        int[] completevisables;
        int viewtype = -1;
        int currentfloatposition = -1;
        private RecyclerView.ViewHolder viewHolder;

        public void setOnFloatClickListener(OnFloatClickListener listener) {
            this.listener = listener;
        }

        public ViewTypeFloatView(ViewGroup container, int viewtype) {
            this.viewtype = viewtype;
            this.container = container;
        }

        @Override
        public void attachRecyclerview(RecyclerView recyclerView) {
            recyclerView.removeOnScrollListener(this);
            recyclerView.addOnScrollListener(this);
            adapter = recyclerView.getAdapter();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int firstcompletevisable = -1;
            if (layoutManager instanceof LinearLayoutManager) {
                firstcompletevisable = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            } else if (layoutManager instanceof GridLayoutManager) {
                ((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                if (completevisables == null) {
                    completevisables = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                }
                ((StaggeredGridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPositions(completevisables);
                firstcompletevisable = completevisables[0];
            }

            /**
             * 找到里的最近的viewtype的position
             */
            int currentnearestposition = -1;
            for (int i = firstcompletevisable - 1; i >= 0; i--) {
                if (adapter.getItemViewType(i) == viewtype) {
                    currentnearestposition = i;
                    break;
                }
            }
            if (currentnearestposition == currentfloatposition) {
                return;
            }
            if (currentnearestposition == -1&&viewHolder != null) {
                    viewHolder.itemView.setVisibility(View.GONE);
                    currentfloatposition = -1;
                    return;
            }

            if (currentfloatposition == -1 && viewHolder == null) {
                currentfloatposition = currentnearestposition;
                viewHolder = adapter.createViewHolder(recyclerView, viewtype);
                if (listener != null) {
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onClick(v, currentfloatposition);
                        }
                    });
                }
                container.addView(viewHolder.itemView);
                adapter.onBindViewHolder(viewHolder, currentfloatposition);
            } else {
                currentfloatposition = currentnearestposition;
                if (listener != null) {
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onClick(v, currentfloatposition);
                        }
                    });
                }
                viewHolder.itemView.setVisibility(View.VISIBLE);
                adapter.onBindViewHolder(viewHolder, currentfloatposition);
            }
        }
    }

    public static class PositionFloatView extends RecyclerView.OnScrollListener implements RecyclerviewFloatHelper.FloatInterface {
        RecyclerView.Adapter adapter;
        OnFloatClickListener listener;
        ViewGroup container;
        int[] completevisables;
        int currentfloatposition = -1;
        int[] floatposition = new int[0];
        private RecyclerView.ViewHolder viewHolder;

        public void setOnFloatClickListener(OnFloatClickListener listener) {
            this.listener = listener;
        }

        public PositionFloatView(ViewGroup container, int... floatposition) {
            this.floatposition = floatposition;
            this.container = container;
        }

        @Override
        public void attachRecyclerview(RecyclerView recyclerView) {
            recyclerView.removeOnScrollListener(this);
            recyclerView.addOnScrollListener(this);
            adapter = recyclerView.getAdapter();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int firstcompletevisable = -1;
            if (layoutManager instanceof LinearLayoutManager) {
                firstcompletevisable = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            } else if (layoutManager instanceof GridLayoutManager) {
                ((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                if (completevisables == null) {
                    completevisables = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                }
                ((StaggeredGridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPositions(completevisables);
                firstcompletevisable = completevisables[0];
            }

            /**
             * 找到里的最近的viewtype的position
             */
            int currentnearestposition = -1;
            for (int i = floatposition.length - 1; i >= 0; i--) {
                if (floatposition[i] < firstcompletevisable) {
                    currentnearestposition = floatposition[i];
                    break;
                }
            }
            if (currentnearestposition == currentfloatposition) {
                return;
            }
            if (viewHolder != null) {
                container.removeView(viewHolder.itemView);
                currentfloatposition = -1;
            }
            if (currentnearestposition == -1) {
                return;
            }
            currentfloatposition = currentnearestposition;
            viewHolder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(currentfloatposition));
            if (listener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClick(v, currentfloatposition);
                    }
                });
            }
            container.addView(viewHolder.itemView);
            adapter.onBindViewHolder(viewHolder, currentfloatposition);
        }
    }
}

