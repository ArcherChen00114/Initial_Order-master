package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.archer.myapplication.R;

import java.util.List;

public class AddRecyclerAdapter extends RecyclerView.Adapter<AddRecyclerAdapter.ViewHolder> {


    private Context context;
    private List<Integer> list;
    public AddRecyclerAdapter(Context context, List<Integer> list) {
        this.context = context;
        this.list = list;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder (View view)
        {
            super(view);
        }

    }
    @Override
    public AddRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(
                context).inflate(R.layout.edit_dialog, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(AddRecyclerAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
