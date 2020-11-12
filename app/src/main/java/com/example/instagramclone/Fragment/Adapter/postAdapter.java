package com.example.instagramclone.Fragment.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.graphics.drawable.AnimatedStateListDrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.bumptech.glide.Glide;
import com.example.instagramclone.CommentActivity;
import com.example.instagramclone.Model.Posts;
import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class postAdapter extends RecyclerView.Adapter<postAdapter.MyViewHolder> {
    public Context context;
    public List<Posts> mPosts;

    private FirebaseUser firebaseUser;

    public postAdapter(Context context, List<Posts> mPosts) {
        this.context = context;
        this.mPosts = mPosts;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater i = LayoutInflater.from(context);
        View v = i.inflate(R.layout.each_post, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Posts posts = mPosts.get(position);
        Glide.with(context).load(posts.getPostedImage()).into(holder.postedImage);

        if (posts.getDescription() == null){
            holder.postCaption.setVisibility(View.GONE);
        }else{
            holder.postCaption.setVisibility(View.VISIBLE);
            holder.postCaption.setText(posts.getDescription());
        }
        PublisherInfo(holder.proPic, (CircleImageView) holder.proPicCmmnt, holder.userNamePost, posts.getPublisher());
        isLiked(posts.getPostId(), holder.like);
        nrLikes(holder.likeCount, posts.getPostId());
        getComments(posts.getPostId(), holder.addCommnt);
//        holder.proPicCmmnt.setImageResource(images[position]);

        final Drawable drawable = holder.likeAnimation.getDrawable();

        holder.postedImage.setOnClickListener(new DoubleClickListener(500) {
            AnimatedVectorDrawableCompat avd;
            AnimatedVectorDrawable avd2;
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDoubleClick() {
                if(holder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(posts.getPostId()).child(firebaseUser.getUid())
                            .setValue(true);
                }
                holder.likeAnimation.setAlpha(0.70f);
                if(drawable instanceof AnimatedStateListDrawableCompat){
                    avd = (AnimatedVectorDrawableCompat) drawable;
                    avd.start();
                }else if(drawable instanceof AnimatedVectorDrawable){
                    avd2 = (AnimatedVectorDrawable) drawable;
                    avd2.start();
                }
            }
        });
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(posts.getPostId()).child(firebaseUser.getUid())
                            .setValue(true);
                } else{
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(posts.getPostId()).child(firebaseUser.getUid())
                            .removeValue();
                }

            }
        });

        holder.addCommnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("PostId", posts.getPostId());
                intent.putExtra("PublisherId", posts.getPublisher());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView userNamePost;
        TextView postCaption;
        TextView addCommnt;
        ImageView postedImage, proPic, proPicCmmnt, likeAnimation;
        ImageView like;
        TextView likeCount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            proPicCmmnt = itemView.findViewById(R.id.proPicCmmnt);
            proPic = itemView.findViewById(R.id.proPic);
            like = itemView.findViewById(R.id.like);
            addCommnt = itemView.findViewById(R.id.addCommnt);
            userNamePost = itemView.findViewById(R.id.userIdPost);
            postCaption = itemView.findViewById(R.id.postCaption);
            postedImage = itemView.findViewById(R.id.postedImage);
            likeCount = itemView.findViewById(R.id.likeCount);
            likeAnimation = itemView.findViewById(R.id.likeAnimation);
        }
    }

    private void getComments(String postId, TextView comments){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(postId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.setText("See all " + snapshot.getChildrenCount() + " comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public abstract class DoubleClickListener implements View.OnClickListener {

        // The time in which the second tap should be done in order to qualify as
        // a double click
        private static final long DEFAULT_QUALIFICATION_SPAN = 200;
        private final long doubleClickQualificationSpanInMillis;
        private long timestampLastClick;

        public DoubleClickListener() {
            doubleClickQualificationSpanInMillis = DEFAULT_QUALIFICATION_SPAN;
            timestampLastClick = 0;
        }

        public DoubleClickListener(long doubleClickQualificationSpanInMillis) {
            this.doubleClickQualificationSpanInMillis = doubleClickQualificationSpanInMillis;
            timestampLastClick = 0;
        }

        @Override
        public void onClick(View v) {
            if((SystemClock.elapsedRealtime() - timestampLastClick) < doubleClickQualificationSpanInMillis) {
                onDoubleClick();
            }
            timestampLastClick = SystemClock.elapsedRealtime();
        }

        public abstract void onDoubleClick();

    }

    private void isLiked(String PostId, ImageView imageView){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes")
                .child(PostId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.ic_liked);
                    imageView.setTag("Liked");
                } else{
                    imageView.setImageResource(R.drawable.ic_unlike);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void nrLikes(TextView likes, String postId){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likes.setText(snapshot.getChildrenCount()+ " likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void PublisherInfo(final ImageView imageProfile, final CircleImageView proPicCmmnt, final TextView textView, String Id){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(Id);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(context).load(user.getImageUrl()).into(imageProfile);
                Glide.with(context).load(user.getImageUrl()).into(proPicCmmnt);
                textView.setText(user.getUserName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {



            }
        });
    }

}
