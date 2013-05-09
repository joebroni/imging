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
import static com.corgrimm.imgy.core.Constants.Extra.OP;

public class CommentsActivity extends BootstrapActivity {

    @Inject protected ObjectMapper objectMapper;

    @InjectView(R.id.comment_list) protected ListView commentsList;
    @InjectView(R.id.username) protected TextView username;
    @InjectView(R.id.points_time) protected TextView pointsTime;
    @InjectView(R.id.comment) protected TextView commentString;
    @InjectView(R.id.op) protected TextView op;

    @InjectExtra(COMMENTS) protected Comment comment;
    @InjectExtra(OP) protected String opId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.comments);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username.setText(comment.getAuthor());
        pointsTime.setText(String.format("%s points", comment.getPoints()));
        commentString.setText(comment.getComment());

        if (opId.equals(comment.getAuthor())) {
            op.setVisibility(View.VISIBLE);
        }
        else {
            op.setVisibility(View.GONE);
        }

        commentsList.setAdapter(new CommentAdapter(CommentsActivity.this, comment.getChildren(), opId));

        setListeners();
    }

    private void setListeners() {


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
