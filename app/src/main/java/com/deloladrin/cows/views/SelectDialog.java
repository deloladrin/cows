package com.deloladrin.cows.views;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.loader.LoaderDialog;
import com.deloladrin.cows.storage.Selection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class SelectDialog<T, V extends View> extends LoaderDialog implements View.OnClickListener
{
    protected TextView title;

    protected RecyclerView container;
    protected RecyclerView.Adapter<Holder<V>> adapter;

    protected Button cancel;

    protected OnEntrySelectedListener<T> onEntrySelectedListener;

    public SelectDialog(Context context)
    {
        super(context);
        this.setContentView(R.layout.global_dialog_select);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
        this.container.setLayoutManager(manager);

        this.cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.cancel))
        {
            this.dismiss();
            return;
        }
    }

    public TextView getTitleView()
    {
        return this.title;
    }

    public RecyclerView getContainerView()
    {
        return this.container;
    }

    public RecyclerView.Adapter<Holder<V>> getAdapter()
    {
        return this.adapter;
    }

    public void setAdapter(RecyclerView.Adapter<Holder<V>> adapter)
    {
        this.adapter = adapter;
        this.container.setAdapter(adapter);
    }

    public OnEntrySelectedListener<T> getOnEntrySelectedListener()
    {
        return this.onEntrySelectedListener;
    }

    public void setOnEntrySelectedListener(OnEntrySelectedListener<T> onEntrySelectedListener)
    {
        this.onEntrySelectedListener = onEntrySelectedListener;
    }

    public static class Holder<T extends View> extends RecyclerView.ViewHolder
    {
        public Holder(T itemView)
        {
            super(itemView);
        }

        public T getItemView()
        {
            return (T) this.itemView;
        }
    }

    public static abstract class Adapter<T, V extends View> extends RecyclerView.Adapter<Holder<V>>
    {
        protected List<T> items;

        public Adapter()
        {
            super();
            this.items = new ArrayList<>();
        }

        public void add(T item)
        {
            int position = this.items.size();

            this.items.add(item);
            this.notifyItemInserted(position);
        }

        public void add(int position, T item)
        {
            this.items.add(position, item);
            this.notifyItemInserted(position);
        }

        public void addAll(Collection<? extends T> items)
        {
            int position = this.items.size();

            this.items.addAll(items);
            this.notifyItemRangeInserted(position, items.size());
        }

        public void addAll(int position, Collection<? extends T> items)
        {
            this.items.addAll(position, items);
            this.notifyItemRangeInserted(position, items.size());
        }

        public void remove(T item)
        {
            int position = this.items.indexOf(item);

            this.items.remove(position);
            this.notifyItemRemoved(position);
        }

        public void remove(int position)
        {
            this.items.remove(position);
            this.notifyItemRemoved(position);
        }

        public T get(int position)
        {
            return this.items.get(position);
        }

        public void clear()
        {
            this.items.clear();
            this.notifyDataSetChanged();
        }

        @Override
        public int getItemCount()
        {
            return this.items.size();
        }

        public List<T> getItems()
        {
            return this.items;
        }
    }

    public static abstract class SelectionAdapter<T, V extends View> extends RecyclerView.Adapter<Holder<V>>
    {
        protected Selection<T> items;

        public SelectionAdapter(Selection<T> items)
        {
            super();
            this.items = items;
        }

        public T get(int position)
        {
            return this.items.get(position);
        }

        @Override
        public int getItemCount()
        {
            return this.items.size();
        }

        public Selection<T> getItems()
        {
            return this.items;
        }
    }

    @FunctionalInterface
    public static interface OnEntrySelectedListener<T>
    {
        void onEntrySelected(T entry);
    }
}
