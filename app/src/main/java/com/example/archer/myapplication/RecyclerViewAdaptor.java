package com.example.archer.myapplication;

/**
 * Created by archer on 2018/4/21.
 */

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.polyak.iconswitch.IconSwitch;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

public class RecyclerViewAdaptor extends RecyclerView.Adapter<RecyclerViewAdaptor.ViewHolder> implements ItemTouchHelperAdaptor{
    private boolean isclick;
    private List<person> list;
    private FloatingActionButton Add;
    private FloatingActionButton Minus;
    private Context context;
    private MyTouchListener mListner;//从Adapter传出需要implements一个click类
    private View.OnClickListener ClickAction;
    private ProgressBar Hpbar;
    private View view;
    private change mChange;
    private Color color;
    private int Change;
    private int ChangeProgress;
    private Interpolator contentInInterpolator;
    private Interpolator contentOutInterpolator;

    public void initAnimationRelatedFields() {


       //contentInInterpolator = new OvershootInterpolator(0.5f);
       //contentOutInterpolator = new DecelerateInterpolator();
    }

    public interface change{
        public void ChangeNum(int fromposition, int toposition);
        public void ChangeNum(int position);

    }
    @Override
    public void onItemMove(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromposition=viewHolder.getAdapterPosition();
        int toposition=target.getAdapterPosition();
        //TODO we have viewholder here, but how can we deal with those tag inside viewholder?
        viewHolder.itemView.findViewById(R.id.Add).setTag(R.id.tag_first,toposition);
        target.itemView.findViewById(R.id.Add).setTag(R.id.tag_first,fromposition);
        viewHolder.itemView.findViewById(R.id.Minus).setTag(R.id.tag_first,toposition);
        target.itemView.findViewById(R.id.Minus).setTag(R.id.tag_first,fromposition);
        viewHolder.itemView.findViewById(R.id.delete).setTag(R.id.tag_first,toposition);
        target.itemView.findViewById(R.id.delete).setTag(R.id.tag_first,fromposition);
        notifyItemMoved(fromposition,toposition);//i guess i need to add one more interface to tell MainActivity that sql need wo swap the data(new ())

        mChange.ChangeNum(fromposition,toposition);


}

    @Override
    public void onItemDissmiss(int position) {
        mChange.ChangeNum(position);
        notifyItemRemoved(position);
        //need to add an interface too
    }

    public interface MyTouchListener{
        public void clickListener(View v,ProgressBar bar);//创建接口
    }
    //a person contains ID,HP,Inti,info,AC
    //and List<person> list make a list of person
    //private InputMethodManager inputManager;

    public RecyclerViewAdaptor( Context context,List<person> list,MyTouchListener listener,change mChange) {
        this.list = list;
        this.context = context;
        this.mListner=listener;//实例化onTouch
        this.mChange=mChange;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(context).inflate(R.layout.item, null);
        RecyclerViewAdaptor.ViewHolder viewHolder = new RecyclerViewAdaptor.ViewHolder(view);
        return viewHolder;
    }

    public void ControlProgressColor(int progress,ProgressBar HPBar){
        if (progress>=50){
            int RedColor=((100-progress)*5);
            ClipDrawable ColorDrawAble=new ClipDrawable(new ColorDrawable(color.argb(255,RedColor,255,0)), Gravity.LEFT,ClipDrawable.HORIZONTAL);
            ColorDrawAble.setLevel(progress*100);
            HPBar.setForeground(ColorDrawAble);
        } else{
            Integer GreenColor=(progress*5);
            ClipDrawable ColorDrawAble=new ClipDrawable(new ColorDrawable(color.argb(255,255,GreenColor,0)), Gravity.LEFT,ClipDrawable.HORIZONTAL);
            ColorDrawAble.setLevel(progress*100);
            HPBar.setForeground(ColorDrawAble);
        }
    }
    @Override
    public void onBindViewHolder(final RecyclerViewAdaptor.ViewHolder holder, final int position) {

        final person model = list.get(position);
        int progress=0;

        try{
            progress = (model.getHp() * 100 / model.getMaxHP());
            ChangeProgress=(Change*100/model.getMaxHP());
        }catch (Exception e){

        }
        holder.Name.setText(model.getName());
        holder.HP.setText(""+model.getHp()+"/"+model.getMaxHP());
        holder.HPBar.setProgress(progress);
        ControlProgressColor(holder.HPBar.getProgress(),holder.HPBar);
        holder.Add.setTag(R.id.tag_first,position);
        holder.Minus.setTag(R.id.tag_first,position);
        holder.delete.setTag(R.id.tag_first,position);
        holder.icon.setTag(R.id.tag_first,position);


    }



    @Override
    public int getItemCount() {
        return list !=null? list.size():0;
    }


    public void ontouch (View v, ProgressBar bar){
        mListner.clickListener(v,bar);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public Switch icon;
        public TextView HP;
        public TextView Name;
        public ProgressBar HPBar;
        public FloatingActionButton Add;
        public FloatingActionButton Minus;
        public TextView modify;
        public TextView delete;


        public ViewHolder(final View itemView) {
            super(itemView);
            initAnimationRelatedFields();
            // modify=(Button)itemView.findViewById(R.id.modify);
            //itemView.setBackgroundColor(Color.parseColor("#483D8B")); purple color
            //itemView.setBackgroundColor(Color.parseColor("#6495ED")); blue color
            icon=itemView.findViewById(R.id.Icon);

            Name = itemView.findViewById(R.id.Name);
            HPBar=itemView.findViewById(R.id.HpBar);
            HP=itemView.findViewById(R.id.HP);
            Add=itemView.findViewById(R.id.Add);
            Minus=itemView.findViewById(R.id.Minus);
            delete=itemView.findViewById(R.id.btnDelete);
            Add.setTag(R.id.tag_second,"Add");
            Minus.setTag(R.id.tag_second,"Minus");
            delete.setTag(R.id.tag_second,"delete");

            delete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    ProgressBar bar=HPBar;
                    ontouch(v,bar);
                }
            });
            Add.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    ProgressBar bar=HPBar;
                    ontouch(v,bar);
                    //TODO make run.start() here
                }
            });
            Minus.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    ProgressBar bar=HPBar;
                    ontouch(v,bar);
                }
            });
            final ValueAnimator ContorlColor=ValueAnimator.ofArgb(color.argb(255,72,61,139),color.argb(255,100,149,237));
            ContorlColor.setDuration(1300);
            ContorlColor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int color=(int)ContorlColor.getAnimatedValue();
                    itemView.setBackgroundColor(color);
                }
            });
            icon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        ContorlColor.start();
                        itemView.setBackgroundColor(Color.parseColor("#6495ED"));
                    }else{
                        ContorlColor.reverse();
                        itemView.setBackgroundColor(Color.parseColor("#483D8B"));
                    }
                }
            });
           /* private void changeContentVisibility() {
                int targetTranslation = 0;
                Interpolator interpolator = null;
                switch (icon.getChecked()) {
                    case LEFT:
                        targetTranslation = 0;
                        interpolator = contentInInterpolator;
                        break;
                    case RIGHT:
                        targetTranslation = content.getHeight();
                        interpolator = contentOutInterpolator;
                        break;
                }
                content.animate().cancel();
                content.animate()
                        .translationY(targetTranslation)
                        .setInterpolator(interpolator)
                        .setDuration(DURATION_COLOR_CHANGE_MS)
                        .start();
            }*/



        }
    }
}
