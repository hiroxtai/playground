package com.github.hiroxtai.android.playground.recyclerviewsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
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

        // 表示するデータとアイテムの View を RecyclerView に紐付け
        recyclerView.setAdapter(new SimpleAdapter(this, data));
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

    private static class SimpleAdapter extends
            RecyclerView.Adapter<ViewHolder> {
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
}
