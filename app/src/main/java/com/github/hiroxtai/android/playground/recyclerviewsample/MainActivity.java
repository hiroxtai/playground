package com.github.hiroxtai.android.playground.recyclerviewsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView =
                (RecyclerView) findViewById(R.id.recycler_view);

        // RecyclerView 自体のレイアウトサイズが変わらない場合、
        // パフォーマンスのために設定しておいた方がよい
        recyclerView.setHasFixedSize(true);

        // Adapter でアイテムが変更（追加・削除など）されたときのアニメーションを指定
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);

        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);
        final int dividerHeight = (int) (4 * getResources().getDisplayMetrics().density);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            // RecyclerView のアイテムが描画される前に呼ばれる
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent,
                    @NonNull RecyclerView.State state) {
                super.onDraw(c, parent, state);
                // アイテムのビューより上に描画される
            }

            // アイテムが描画されたあとに呼ばれる
            @Override
            public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent,
                    @NonNull RecyclerView.State state) {
                super.onDrawOver(c, parent, state);
                // アイテムのビューより下に描画される

                final RecyclerView.LayoutManager manager = parent.getLayoutManager();
                final int left = parent.getPaddingLeft();
                final int right = parent.getWidth() - parent.getPaddingRight();
                final int childCount = parent.getChildCount();
                for (int i = 1; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    final RecyclerView.LayoutParams params =
                            (RecyclerView.LayoutParams) child.getLayoutParams();
                    if (params.getViewLayoutPosition() == 0) {
                        continue;
                    }

                    // ViewCompat.getTranslationY() を入れないと
                    // 追加・削除のアニメーション時の位置が変になる
                    final int top = manager.getDecoratedTop(child)
                            - params.topMargin
                            + Math.round(ViewCompat.getTranslationY(child));
                    final int bottom = top + dividerHeight;
                    c.drawRect(left, top, right, bottom, paint);
                }
            }

            // 第1引数のRectに上下左右のオフセットを指定
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                    @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

                // アイテム間のオフセットが2つ分になってしまうので、
                // View の位置によってオフセット値を変える
                int position =
                        ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();

                // 単に区切り線を入れる場合
                int top = position == 0 ? 0 : dividerHeight;
                outRect.set(0, top, 0, 0);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            data.add("Item : " + i);
        }

        // selectableItemBackground に指定されているリソースID の値を取得しておく
        TypedValue val = new TypedValue();
        if (getTheme() != null) {
            getTheme().resolveAttribute(
                    android.R.attr.selectableItemBackground, val, true);
        }
        final int backgroundResId = val.resourceId;

//        // 表示するデータとアイテムのView をRecyclerView に紐付け
//        final SimpleAdapter adapter = new SimpleAdapter(this, data) {
//            @Override
//            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                final ViewHolder viewHolder
//                        = super.onCreateViewHolder(parent, viewType);
//                viewHolder.itemView.setBackgroundResource(backgroundResId);
//                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int position = viewHolder.getAdapterPosition();
//                        Toast.makeText(v.getContext(), "clicked " + position,
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//                return viewHolder;
//            }
//        };
//
//        // 表示するデータとアイテムの View を RecyclerView に紐付け
//        recyclerView.setAdapter(adapter);

//        // ヘッダー、フッターあり
//        ImageView header = new ImageView(this);
//        header.setLayoutParams(new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, 200));
//        header.setBackgroundColor(Color.RED);
//        ImageView footer = new ImageView(this);
//        footer.setLayoutParams(new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, 200));
//        footer.setBackgroundColor(Color.BLUE);
//
//        // 表示するデータとアイテムの View を RecyclerView に紐付け
//        final SimpleAdapter2 adapter =
//                new SimpleAdapter2(this, header, footer, data);
//        recyclerView.setAdapter(adapter);

        // 表示するデータとアイテムの View を RecyclerView に紐付け
        final SimpleAdapter3 adapter = new SimpleAdapter3(this, data) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final ViewHolder viewHolder
                        = super.onCreateViewHolder(parent, viewType);
                viewHolder.itemView.setBackgroundResource(backgroundResId);
                viewHolder.itemView.setOnClickListener(v -> {
                    int position = viewHolder.getAdapterPosition();
                    remove(position);
                });
                return viewHolder;
            }
        };
        recyclerView.setAdapter(adapter);

        // スワイプによる削除とドラッグによる移動
        int dragDirs = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(dragDirs, ItemTouchHelper.RIGHT) {
                    // ドラッグ中の色を変える
                    @Override
                    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                            int actionState) {
                        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
                        }
                        super.onSelectedChanged(viewHolder, actionState);
                    }

                    @Override
                    public void clearView(RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder) {
                        super.clearView(recyclerView, viewHolder);
                        viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
                    }

                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            RecyclerView.ViewHolder target) {
                        int from = viewHolder.getAdapterPosition();
                        int to = target.getAdapterPosition();
                        adapter.move(from, to);
                        return true;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        adapter.remove(position);
                    }
                });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        static final int LAYOUT_ID = android.R.layout.simple_list_item_1;
        final TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);
            textView.setBackgroundColor(Color.LTGRAY);
        }
    }

    private static class SimpleAdapter extends RecyclerView.Adapter<ViewHolder> {
        private final LayoutInflater inflater;
        private final List<String> data;

        private SimpleAdapter(Context context, List<String> data) {
            this.inflater = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(
                    inflater.inflate(ViewHolder.LAYOUT_ID, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String text = data.get(position);
            holder.textView.setText(text);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private static class SimpleAdapter2 extends HeaderViewListAdapter<ViewHolder> {
        private final LayoutInflater inflater;
        private final List<String> data;

        public SimpleAdapter2(Context context, View headerView, View footerView,
                List<String> data) {
            super(headerView, footerView);
            this.inflater = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        protected ViewHolder onCreateItemViewHolder(ViewGroup parent) {
            return new ViewHolder(
                    inflater.inflate(ViewHolder.LAYOUT_ID, parent, false));
        }

        @Override
        protected void onBindItemViewHolder(ViewHolder holder, int position) {
            String text = data.get(position);
            holder.textView.setText(text);
        }

        @Override
        protected int getAdapterItemCount() {
            return data.size();
        }
    }

    private static abstract class HeaderViewListAdapter<VH extends RecyclerView.ViewHolder>
            extends RecyclerView.Adapter {
        protected abstract VH onCreateItemViewHolder(ViewGroup parent);

        protected abstract void onBindItemViewHolder(VH holder, int position);

        protected abstract int getAdapterItemCount();

        private static class HeaderFooterHolder extends RecyclerView.ViewHolder {
            public HeaderFooterHolder(View itemView) {
                super(itemView);
            }
        }

        public static final int ITEM_VIEW_TYPE_ITEM = 0;
        // ヘッダー用
        public static final int ITEM_VIEW_TYPE_HEADER = 1;
        // フッター用
        public static final int ITEM_VIEW_TYPE_FOOTER = 2;
        private final View headerView;

        private final View footerView;

        public HeaderViewListAdapter(View headerView, View footerView) {
            this.headerView = headerView;
            this.footerView = footerView;
        }

        @Override
        public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                int viewType) {
            switch (viewType) {
                case ITEM_VIEW_TYPE_HEADER:
                    return new HeaderFooterHolder(headerView);
                case ITEM_VIEW_TYPE_FOOTER:
                    return new HeaderFooterHolder(footerView);
                case ITEM_VIEW_TYPE_ITEM:
                    return onCreateItemViewHolder(parent);
            }
            return null;
        }

        @Override
        public final void onBindViewHolder(RecyclerView.ViewHolder holder,
                int position) {
            if (holder.getItemViewType() == ITEM_VIEW_TYPE_ITEM) {
                onBindItemViewHolder((VH) holder, position - getHeadersCount());
            }
        }

        private int getHeadersCount() {
            return headerView != null ? 1 : 0;
        }

        private int getFootersCount() {
            return footerView != null ? 1 : 0;
        }

        @Override
        public final int getItemCount() {
            return getHeadersCount() + getFootersCount() + getAdapterItemCount();
        }

        // Positionに対応するViewTypeを返す
        @Override
        public final int getItemViewType(int position) {
            int numHeaders = getHeadersCount();
            if (position < numHeaders) {
                return ITEM_VIEW_TYPE_HEADER;
            }
            int adjPosition = position - numHeaders;
            int adapterCount = getAdapterItemCount();
            if (adjPosition < adapterCount) {
                return ITEM_VIEW_TYPE_ITEM;
            }
            return ITEM_VIEW_TYPE_FOOTER;
        }
    }

    private static class SimpleAdapter3 extends RecyclerArrayAdapter<String, ViewHolder> {
        private final LayoutInflater inflater;
        private final List<String> data;

        public SimpleAdapter3(Context context, List<String> data) {
            super(data);
            this.inflater = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(
                    inflater.inflate(ViewHolder.LAYOUT_ID, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String text = data.get(position);
            holder.textView.setText(text);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private static abstract class RecyclerArrayAdapter<T, VH extends RecyclerView.ViewHolder>
            extends RecyclerView.Adapter<VH> {
        private final Object lock = new Object();
        private final List<T> objects;

        public RecyclerArrayAdapter() {
            this(new ArrayList<T>());
        }

        public RecyclerArrayAdapter(List<T> objects) {
            this.objects = objects;
        }

        public void add(@NonNull T object) {
            final int position = objects.size();
            synchronized (lock) {
                objects.add(object);
            }
            notifyItemInserted(position);
        }

        public void addAll(@NonNull Collection<? extends T> collection) {
            final int itemCount = collection.size();
            final int positionStart = objects.size();
            synchronized (lock) {
                objects.addAll(collection);
            }
            notifyItemRangeInserted(positionStart, itemCount);
        }

        public void remove(@NonNull T object) {
            int position = objects.indexOf(object);
            synchronized (lock) {
                objects.remove(object);
            }
            notifyItemRemoved(position);
        }

        public T remove(int position) {
            T prev;
            synchronized (lock) {
                prev = objects.remove(position);
            }
            notifyItemRemoved(position);
            return prev;
        }

        public void move(int from, int to) {
            synchronized (lock) {
                T prev = objects.remove(from);
                objects.add(to > from ? to - 1 : to, prev);
            }
            notifyItemMoved(from, to);
        }
    }
}
