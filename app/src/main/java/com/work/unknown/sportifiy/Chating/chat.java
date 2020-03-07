package com.work.unknown.sportifiy.Chating;

import android.app.ActionBar;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
//import com.firebase.ui.database.FirebaseListAdapter;
//import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;
import com.work.unknown.sportifiy.Adapter.messageAdapter;
import com.work.unknown.sportifiy.R;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class chat extends AppCompatActivity {
    ImageView sendmessage;
    EmojiconEditText messagebody;
    DatabaseReference chat;
    String Current_user_id;
    DatabaseReference rootRef;
    String key;
    Boolean curr_user=false;
    int i=0;
    RecyclerView listView;
    messageAdapter adapter;
    AVLoadingIndicatorView avi;
    ArrayList<chatingmessage>message=new ArrayList<>();
    ArrayList<chatingmessage>new_message=new ArrayList<>();
    ImageView emojibtn;
    View rootView;
    EmojIconActions emojIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        avi=findViewById(R.id.chatme);
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(date);
        TextView datetext=findViewById(R.id.rrp);
        datetext.setText(formattedDate);
        emojibtn=findViewById(R.id.emoji_btn);
        messagebody=findViewById(R.id.emojicon_edit_text);
        sendmessage=findViewById(R.id.submit_btn);
        rootView = findViewById(R.id.chatactivity);
        //////////////foremoji//////////////////////////
        avi.show();
        sendmessage.setEnabled(false);
        emojIcon = new EmojIconActions(this, rootView, messagebody, emojibtn);
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
            }

            @Override
            public void onKeyboardClose() {
            }
        });
        ////////////////////// display All previous messages /////////////////////////////

        ///////////////////////////////////////////////////////////////////////////////
        listView=findViewById(R.id.chatmessages);
       Current_user_id=FirebaseAuth.getInstance().getCurrentUser().getUid();
        key=getIntent().getExtras().getString("user_Key");


        rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("Chatting")) {
                    create();
                }
                else
                 {
                        chat=FirebaseDatabase.getInstance().getReference().child("Chatting").child(Current_user_id).child(key);
                        Toast.makeText(chat.this, "From Top", Toast.LENGTH_SHORT).show();
                     displaymessage();
                 }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
              sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String body=messagebody.getText().toString();
                if(key!=null&& !TextUtils.isEmpty(messagebody.getText().toString())) {
                    chat.push().setValue(new chatingmessage(body,Current_user_id));

                }
                messagebody.setText("");

            }
        });

    }
    public void create(){
      DatabaseReference  Ref = FirebaseDatabase.getInstance().getReference();
        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("Chatting").child(key).child(Current_user_id).exists())
                {
                    Toast.makeText(chat.this, "NotFound", Toast.LENGTH_SHORT).show();
                    chat=FirebaseDatabase.getInstance().getReference().child("Chatting").child(Current_user_id).child(key);
                    curr_user=true;
                    displaymessage();

                }else{
                    Toast.makeText(chat.this, "Found", Toast.LENGTH_SHORT).show();
                  chat=FirebaseDatabase.getInstance().getReference().child("Chatting").child(key).child(Current_user_id);
                    displaymessage();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



    }
    private void displaymessage()
    {
        avi.hide();
       sendmessage.setEnabled(true);
        final FirebaseRecyclerAdapter<chatingmessage,ContactViewHolder> adapter = new FirebaseRecyclerAdapter<chatingmessage, ContactViewHolder>(
                chatingmessage.class,
                R.layout.mymessage,
                ContactViewHolder.class,
                chat
        )
        {
            @Override
            protected void populateViewHolder(final ContactViewHolder viewHolder, final chatingmessage model, int position) {
              String from=model.getFrom();
                viewHolder.messageText.setText(model.getMessageText());
            };
            private boolean isSender(int position) {
               return Current_user_id.equals(getItem(position).getFrom());
            }
            @Override
            public int getItemViewType(int position) {

                if(isSender(position))
                {
                    return R.layout.mymessage;
                }
                else
                {
                    return R.layout.frindmessage;
                }

                //return super.getItemViewType(position);
            }

        };
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                listView.smoothScrollToPosition(adapter.getItemCount());
            }
        });





    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void displaymessage2() {
        Query lastQuery = chat;
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatingmessage m;
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    String mess = d.child("messageText").getValue().toString();
                    String from = d.child("from").getValue().toString();
                    m = new chatingmessage(mess, from);
                    message.add(m);
                    adapter.notifyDataSetChanged();
                    listView.scrollToPosition(message.size() - 1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        });
    }
    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView messageText;

        View mview;
        public ContactViewHolder(View v) {
            super(v);
            mview=v;
            messageText =v.findViewById(R.id.messagetextView);

        }
    }
}
