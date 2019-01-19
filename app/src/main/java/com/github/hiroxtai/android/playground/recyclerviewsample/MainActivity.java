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

        final int offset = (int) (8 * getResources().getDisplayMetrics().density);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            // 第1引数のRectに上下左右のオフセットを指定
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                    @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

                // アイテム間のオフセットが2つ分になってしまうので、
                // View の位置によってオフセット値を変える
                int position =
                        ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();

                // 上下左右にオフセットを入れる場合
                if (position == 0) {
                    outRect.set(offset, offset, offset, offset);
                } else {
                    outRect.set(offset, 0, offset, offset);
                }
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
