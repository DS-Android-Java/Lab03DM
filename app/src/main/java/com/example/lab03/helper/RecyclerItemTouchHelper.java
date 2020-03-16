package com.example.lab03.helper;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab03.adaptador.AdaptadorCurso;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private RecyclerItemTouchHelperListener listener;
    private View foregroundView;
    private View backgroundViewEdit;
    private View backgroundViewDelete;
    private int dragColor = Color.rgb(102, 102, 255);

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            //check adapter
            if (this.listener.getClass().getSimpleName().equals("MantenimientoCursoActivity")) {
                backgroundViewEdit = ((AdaptadorCurso.MyViewHolder) viewHolder).viewBackgroundEdit;
                backgroundViewDelete = ((AdaptadorCurso.MyViewHolder) viewHolder).viewBackgroundDelete;
                foregroundView = ((AdaptadorCurso.MyViewHolder) viewHolder).viewForeground;
            }/* else if (this.listener.getClass().getSimpleName().equals("AdmCarreraActivity")) {
                backgroundViewEdit = ((AdaptadorProfesor.MyViewHolder) viewHolder).viewBackgroundEdit;
                backgroundViewDelete = ((AdaptadorProfesor.MyViewHolder) viewHolder).viewBackgroundDelete;
                foregroundView = ((AdaptadorProfesor.MyViewHolder) viewHolder).viewForeground;
            }*/

            //Selected item
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                //fancy color picked
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), Color.WHITE, dragColor);
                colorAnimation.setDuration(250); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        foregroundView.setBackgroundColor((int) animator.getAnimatedValue());
                    }
                });
                colorAnimation.start();
            }
            getDefaultUIUtil().onSelected(foregroundView);
            super.onSelectedChanged(viewHolder, actionState);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //clear view with fancy animation
        int color = Color.TRANSPARENT;
        Drawable background = foregroundView.getBackground();
        if (background instanceof ColorDrawable)
            color = ((ColorDrawable) background).getColor();
        //check color
        if (color == dragColor) {
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), dragColor, Color.WHITE);
            colorAnimation.setDuration(250); // milliseconds
            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    foregroundView.setBackgroundColor((int) animator.getAnimatedValue());
                }
            });
            colorAnimation.start();
        }
        super.clearView(recyclerView, viewHolder);
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            drawBackground(dX);
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }
    }

    private void drawBackground(float dX) {
        if (this.listener.getClass().getSimpleName().equals("MatriculaActivity")) {
            backgroundViewDelete.setVisibility(View.VISIBLE);
        } else {
            if (dX > 0) {
                backgroundViewEdit.setVisibility(View.VISIBLE);
                backgroundViewDelete.setVisibility(View.GONE);
            } else {
                backgroundViewDelete.setVisibility(View.VISIBLE);
                backgroundViewEdit.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);

        void onItemMove(int source, int target);
    }
}
