package com.corgrimm.imgy.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.corgrimm.imgy.R;
import com.corgrimm.imgy.api.ImgyApi;
import com.corgrimm.imgy.models.Comment;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static com.corgrimm.imgy.core.Constants.Extra.COMMENTS;
import static com.corgrimm.imgy.core.Constants.Extra.OP;
import static com.corgrimm.imgy.core.Constants.Oauth.EXPIRED_TOKEN;
import static com.corgrimm.imgy.core.Constants.Oauth.IMGUR_CLIENT_ID;
import static com.corgrimm.imgy.core.Constants.Oauth.TOKEN_VALID;
import static com.corgrimm.imgy.core.Constants.Vote.*;

/**
 * Created with IntelliJ IDEA.
 * User: Anderson
 * Date: 3/22/13
 * Time: 9:08 AM
 */
public class CommentAdapter extends BaseAdapter {

    private Context ctx;
    List<Comment> comments;
    int vote_status;
    String opId;

    public CommentAdapter(Context c, List<Comment> comments, String opId) {
        ctx = c;
        this.comments = comments;
        this.opId = opId;
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

        final Comment comment = comments.get(position);

        TextView username = (TextView) commentView.findViewById(R.id.username);
        TextView points_time = (TextView) commentView.findViewById(R.id.points_time);
        TextView commentString = (TextView) commentView.findViewById(R.id.comment);
        TextView op = (TextView) commentView.findViewById(R.id.op);

        if (opId.equals(comment.getAuthor())) {
            op.setVisibility(View.VISIBLE);
        }
        else {
            op.setVisibility(View.GONE);
        }

        username.setText(comment.getAuthor());
        points_time.setText(String.format("%s points", comment.getPoints().toString()));
        commentString.setText(comment.getComment());


        final ImageButton upvote = (ImageButton) commentView.findViewById(R.id.upvote);
        final ImageButton downvote = (ImageButton) commentView.findViewById(R.id.downvote);

        if (comment.getVote() != null) {
            if (comment.getVote().equals(UP_VOTE_STRING)) {
                upvote.setImageDrawable(ctx.getResources().getDrawable(R.drawable.up_green_256));
                vote_status = UPVOTE;
            }
            else if (comment.getVote().equals(DOWN_VOTE_STRING)) {
                downvote.setImageDrawable(ctx.getResources().getDrawable(R.drawable.down_red_256));
                vote_status = DOWNVOTE;
            }
        }
        else {
            downvote.setImageDrawable(ctx.getResources().getDrawable(R.drawable.down_white_256));
            upvote.setImageDrawable(ctx.getResources().getDrawable(R.drawable.up_white_256));
        }

        upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (vote_status == UPVOTE) {
//                    upvote.setImageDrawable(getResources().getDrawable(R.drawable.up_white_256));
//                    vote_status = NO_VOTE;
//                }
//                else {
                if (vote_status == DOWNVOTE) {
                    downvote.setImageDrawable(ctx.getResources().getDrawable(R.drawable.down_white_256));
                }
                upvote.setImageDrawable(ctx.getResources().getDrawable(R.drawable.up_green_256));
                vote_status = UPVOTE;
                voteOnComment(comment, UP_VOTE_STRING);
//                }
            }
        });

        downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (vote_status == DOWNVOTE) {
//                    downvote.setImageDrawable(getResources().getDrawable(R.drawable.down_white_256));
//                    vote_status = NO_VOTE;
//                }
//                else {
                if (vote_status == UPVOTE) {
                    upvote.setImageDrawable(ctx.getResources().getDrawable(R.drawable.up_white_256));
                }
                downvote.setImageDrawable(ctx.getResources().getDrawable(R.drawable.down_red_256));
                vote_status = DOWNVOTE;
                voteOnComment(comment, DOWN_VOTE_STRING);
//                }
            }
        });

        commentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (comment.getChildren().size() > 0) {
                    ctx.startActivity(new Intent(ctx, CommentsActivity.class).putExtra(COMMENTS, comment).putExtra(OP, opId));
                }
            }
        });

        return commentView;

    }

    private void voteOnComment(final Comment comment, final String vote) {
        int tokenStatus = ImgyApi.checkForValidAuthToken(ctx);
        if ( tokenStatus == TOKEN_VALID) {
            ImgyApi.voteForComment(ctx, comment.getId(), vote, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);

                }

                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    super.onFailure(e, errorResponse);

                }

                @Override
                public void onFailure(Throwable e, JSONArray errorResponse) {
                    super.onFailure(e, errorResponse);
                }
            });
        }
        else if (tokenStatus == EXPIRED_TOKEN) {
            ImgyApi.getAccessTokenFromRefresh(ctx, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    super.onFailure(e, errorResponse);
                }

                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);
                    ImgyApi.saveAuthToken(ctx, response);
                    voteOnComment(comment, vote);
                }
            });
        }
        else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(
                    Uri.parse(String.format("https://api.imgur.com/oauth2/authorize?client_id=%s&response_type=%s&state=%s", IMGUR_CLIENT_ID, "code", "useless")));
            ctx.startActivity(intent);
        }
    }
}
