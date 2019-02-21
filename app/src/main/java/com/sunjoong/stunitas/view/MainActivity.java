package com.sunjoong.stunitas.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.sunjoong.stunitas.R;
import com.sunjoong.stunitas.define.Define;
import com.sunjoong.stunitas.model.data.ImageInfo;
import com.sunjoong.stunitas.presenter.MainContract;
import com.sunjoong.stunitas.presenter.MainPresenter;
import com.sunjoong.stunitas.view.adapter.ImagePagerAdapter;
import com.sunjoong.stunitas.view.fragment.ImageFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainContract.View
{

    private MainPresenter mPresenter;

    private ViewPager mViewPager;
    private ImagePagerAdapter mPagerAdapter;
    private EditText mTextKeyword;
    private ProgressBar mLoadingView;

    private InputMethodManager mKeyboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mViewPager = (ViewPager) findViewById(R.id.view_pager_image);
        mTextKeyword = (EditText) findViewById(R.id.edit_keyword);
        mTextKeyword.addTextChangedListener(mTextWatcher);
        mTextKeyword.requestFocus();
        mLoadingView = (ProgressBar) findViewById(R.id.loading_view);

        mKeyboardManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mPresenter = new MainPresenter(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        mMainHandler.sendEmptyMessageDelayed(MSG_SHOW_INPUT_METHOD, DELAY_SHOW_INPUT_METHOD);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        mMainHandler.sendEmptyMessage(MSG_HIDE_INPUT_METHOD);
    }

    @Override
    public void setImages(ArrayList<ImageInfo> imageList, int page)
    {
        mLoadingView.setVisibility(View.INVISIBLE);

        initViewPager(imageList, page);
    }

    @Override
    public void setError()
    {
        retryDialog(getResources().getString(R.string.guide_error));
    }

    private void initViewPager(ArrayList<ImageInfo> imageList, int page)
    {
        ArrayList<ImageFragment> fragments = new ArrayList<>();

        if(imageList != null)
        {
            if(imageList.size() == 0)
                retryDialog(getResources().getString(R.string.guide_no_result));

            for(ImageInfo info : imageList)
                fragments.add(new ImageFragment(this, info.getImagePath()));
        }

        mPagerAdapter = new ImagePagerAdapter(getSupportFragmentManager(), fragments, mOnPageListener);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnPageChangeListener(mPagerAdapter);

        if(page > 0)
            mViewPager.setCurrentItem((page - 1) * Define.DEFAULT_PAGE_SIZE);
    }

    private ImagePagerAdapter.OnPageListener mOnPageListener = new ImagePagerAdapter.OnPageListener()
    {
        @Override
        public void onArrived()
        {
            mMainHandler.sendEmptyMessage(MSG_ADD_IMAGE);
        }
    };

    private TextWatcher mTextWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

        }

        @Override
        public void afterTextChanged(Editable s)
        {
            if(mMainHandler.hasMessages(MSG_GET_IMAGE))
                mMainHandler.removeMessages(MSG_GET_IMAGE);

            mMainHandler.sendEmptyMessageDelayed(MSG_GET_IMAGE, DELAY_GET_IMAGE);
        }
    };

    private void retryDialog(String comment)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(comment)
               .setNegativeButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener()
               {
                   @Override
                   public void onClick(DialogInterface dialog, int which)
                   {
                       mMainHandler.sendEmptyMessage(MSG_SHOW_INPUT_METHOD);
                       mTextKeyword.setText("");
                   }
               })
               .create()
               .show();
    }

    public final static int MSG_GET_IMAGE = 1000;
    public final static int MSG_ADD_IMAGE = MSG_GET_IMAGE + 1;
    public final static int MSG_SHOW_INPUT_METHOD = MSG_ADD_IMAGE + 1;
    public final static int MSG_HIDE_INPUT_METHOD = MSG_SHOW_INPUT_METHOD + 1;

    private final static int DELAY_GET_IMAGE = 1000;
    private final static int DELAY_SHOW_INPUT_METHOD = 700;

    private MainHandler mMainHandler = new MainHandler();

    private class MainHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case MSG_GET_IMAGE:

                    if(mTextKeyword.getText().length() == 0)
                        initViewPager(null, 0);
                    else
                    {
                        if(mPresenter != null)
                            mPresenter.getImages(mTextKeyword.getText().toString());

                        mLoadingView.setVisibility(View.VISIBLE);
                        sendEmptyMessage(MSG_HIDE_INPUT_METHOD);
                    }
                    break;

                case MSG_ADD_IMAGE:
                    if(mPresenter != null)
                        mPresenter.addImages();
                    break;

                case MSG_SHOW_INPUT_METHOD:
                    mKeyboardManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    break;

                case MSG_HIDE_INPUT_METHOD:
                    mKeyboardManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    break;

                default:
                    break;
            }
        }
    }

}
