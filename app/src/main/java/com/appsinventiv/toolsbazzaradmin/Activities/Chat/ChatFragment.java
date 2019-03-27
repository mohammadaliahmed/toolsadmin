package com.appsinventiv.toolsbazzaradmin.Activities.Chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.appsinventiv.toolsbazzaradmin.Adapters.ChatAdapter;
import com.appsinventiv.toolsbazzaradmin.Adapters.ChatListAdapter;
import com.appsinventiv.toolsbazzaradmin.Adapters.OrdersAdapter;
import com.appsinventiv.toolsbazzaradmin.Interfaces.TabCountCallbacks;
import com.appsinventiv.toolsbazzaradmin.Models.ChatModel;
import com.appsinventiv.toolsbazzaradmin.Models.OrderModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.SharedPrefs;
import com.appsinventiv.toolsbazzaradmin.Utils.SwipeControllerActions;
import com.appsinventiv.toolsbazzaradmin.Utils.SwipeToDeleteCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ChatFragment extends Fragment {

    Context context;
    RecyclerView recyclerview;
    ArrayList<ChatModel> itemList = new ArrayList<>();
    ChatListAdapter adapter;
    DatabaseReference mDatabase;
    String chatWith;

    TabCountCallbacks callbacks;
    int chatCount = 0;
    private SwipeToDeleteCallback swipeController;

    public ChatFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ChatFragment(String chatWith, TabCountCallbacks callbacks) {
        // Required empty public constructor
        this.chatWith = chatWith;
        this.callbacks = callbacks;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootView = inflater.inflate(R.layout.chat_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_orders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatListAdapter(context, itemList, chatWith);


        recyclerView.setAdapter(adapter);
        swipeController = new SwipeToDeleteCallback(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {

//                markOrderAsDeleted(arrayList.get(position).getOrderId());

            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });



        return rootView;

    }


    @Override
    public void onResume() {
        super.onResume();
//        if(chatWith.equalsIgnoreCase("wholesale")){
//        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        final String chatName = chatWith + "Chats";
        mDatabase.child("Chats").child(chatName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                chatCount = 0;
                if (dataSnapshot.getValue() != null) {
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        getUnreadCount("Chats/" + chatName + "/" + snapshot.getKey());
                        mDatabase.child("Chats").child(chatName).child(snapshot.getKey()).limitToLast(1).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    for (DataSnapshot abc : dataSnapshot.getChildren()) {
                                        ChatModel model = abc.getValue(ChatModel.class);
                                        if (model != null) {

                                            itemList.add(model);


                                            Collections.sort(itemList, new Comparator<ChatModel>() {
                                                @Override
                                                public int compare(ChatModel listData, ChatModel t1) {
                                                    Long ob1 = listData.getTime();
                                                    Long ob2 = t1.getTime();

                                                    return ob2.compareTo(ob1);

                                                }
                                            });
                                            adapter.notifyDataSetChanged();
                                        }
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void getUnreadCount(String s) {
        mDatabase.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot abc : dataSnapshot.getChildren()) {
                        ChatModel model = abc.getValue(ChatModel.class);
                        if (model != null) {
                            if (!model.getUsername().equalsIgnoreCase(SharedPrefs.getUsername())) {
                                if (!model.getStatus().equalsIgnoreCase("read")) {
                                    chatCount = chatCount + 1;

                                }
                            }

//                            itemList.add(model);

                        }
                    }
                    callbacks.newCount(chatCount, 0);
                    setChatCount();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setChatCount() {
        SharedPrefs.setChatCount("" + chatCount);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


}
