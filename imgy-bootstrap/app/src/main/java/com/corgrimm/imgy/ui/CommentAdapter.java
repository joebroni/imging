package com.corgrimm.imgy.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.corgrimm.imgy.R;
import com.corgrimm.imgy.models.Comment;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Anderson
 * Date: 3/22/13
 * Time: 9:08 AM
 */
public class CommentAdapter extends BaseAdapter {

    private Context ctx;
    List<Comment> comments;

    public CommentAdapter(Context c, List<Comment> comments) {
        ctx = c;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }


    @Override
    public Object getItem(int arg0) {
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View commentView = convertView;
        if (commentView == null) {
            commentView = LayoutInflater.from(ctx).inflate(R.layout.comment, parent, false);
        }

        Comment comment = comments.get(position);

        TextView username = (TextView) commentView.findViewById(R.id.username);
        TextView points_time = (TextView) commentView.findViewById(R.id.points_time);
        TextView commentString = (TextView) commentView.findViewById(R.id.comment);

        username.setText(comment.getAuthor());
        points_time.setText(String.format("%s points", comment.getPoints().toString()));
        commentString.setText(comment.getComment());

        return commentView;

    }
}
