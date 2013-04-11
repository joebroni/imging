package com.corgrimm.imgy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.corgrimm.imgy.R;
import com.corgrimm.imgy.models.Comment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

import static com.corgrimm.imgy.core.Constants.Extra.COMMENTS;

public class CommentsActivity extends BootstrapActivity {

    @Inject protected ObjectMapper objectMapper;

    @InjectView(R.id.comment_list) protected ListView commentsList;
    @InjectView(R.id.username) protected TextView username;
    @InjectView(R.id.points_time) protected TextView pointsTime;
    @InjectView(R.id.comment) protected TextView commentString;

    @InjectExtra(COMMENTS) protected Comment comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.comments);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username.setText(comment.getAuthor());
        pointsTime.setText(String.format("%s points", comment.getPoints()));
        commentString.setText(comment.getComment());

        commentsList.setAdapter(new CommentAdapter(CommentsActivity.this, comment.getChildren()));

        setListeners();
    }

    private void setListeners() {
        commentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Comment childComment = comment.getChildren().get(i);
                if (childComment.getChildren().size() > 0) {
                    startActivity(new Intent(CommentsActivity.this, CommentsActivity.class).putExtra(COMMENTS, childComment));
                }
            }
        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getSupportMenuInflater();
//        inflater.inflate(R.menu.imgy_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.comments:
//                menu.toggle(true);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

}
