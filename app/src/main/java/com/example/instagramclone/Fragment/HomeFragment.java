package com.example.instagramclone.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagramclone.Fragment.Adapter.storyAdapter;
import com.example.instagramclone.Fragment.Adapter.postAdapter;
import com.example.instagramclone.Model.Posts;
import com.example.instagramclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment{
    RecyclerView rView, rViewPost;
    List<Posts> PostLists;
    postAdapter adapter1;
    private List<String> followingList;

    String[] s1;

    int[] images = {R.drawable.ic_story, R.drawable.duck, R.drawable.person1, R.drawable.person2, R.drawable.person3, R.drawable.person4,
            R.drawable.person5, R.drawable.person6, R.drawable.person7, R.drawable.person8};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        rView = v.findViewById(R.id.rView);
        rViewPost = v.findViewById(R.id.rViewPost);
        s1 = getResources().getStringArray(R.array.languageList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rView.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearPost = new LinearLayoutManager(getContext());
        linearPost.setOrientation(LinearLayoutManager.VERTICAL);
        PostLists = new ArrayList<>();
        rViewPost.setLayoutManager(linearPost);

        adapter1 = new postAdapter(getContext(), PostLists);
        rViewPost.setAdapter(adapter1);
        checkFolowing();

        storyAdapter adapter = new storyAdapter(getContext(), s1, images);
        rView.setAdapter(adapter);


        return v;

    }

    private void checkFolowing(){
        followingList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followingList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    followingList.add(dataSnapshot.getKey());
                }
                readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostLists.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Posts posts = dataSnapshot.getValue(Posts.class);
                    for (String id : followingList){
                        if(posts.getPublisher().equals(id)){
                            PostLists.add(posts);
                        }
                    }
                }
                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}