package com.codesniper.looperscrollview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codesniper.looperscrollview.view.TalkView;
import com.codesniper.looperscrollview.R;
import com.codesniper.looperscrollview.bean.TalkBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TalkView mTalkView;

    private String[] str=new String[]
            {"https://i2.hdslb.com/bfs/face/d073dbd50b729138f7253f3258ac6ef67bddd741.jpg",
                    "https://i0.hdslb.com/bfs/face/15e41493b1db34f76b25b5bd9c73e4976a3b9128.jpg",
                    "https://i0.hdslb.com/bfs/face/f40b734ef61f95f8adb3beca5b7b693db399c50e.jpg",
                    "https://i0.hdslb.com/bfs/face/85a7a5974f67cca08e1eff2ffd46b50488f8dc97.jpg",
                    "https://i0.hdslb.com/bfs/face/156d5d3b3f4b66d940365b3b0e3a809e1fcc0d97.jpg",
            };

    private String[] content=new String[]{
           "天天卡牌", "老佳毛人", "半藏芒果冰", "骚客鸡坤","成都养鸡场场长"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TalkBean talkBean=new TalkBean();
        List<TalkBean.AccountDesListBean> list=new ArrayList<>();
        for(int i=0;i<str.length;i++){
            TalkBean.AccountDesListBean item=new TalkBean.AccountDesListBean();
            item.setJob("Android开发者");
            item.setBalance("2019.88");
            item.setContent("当前选中下标:"+i+"\n内容："+content[i]);
            item.setNickName("CodeSniper");
            item.setAddress("广州");
            item.setAvatarUrl(str[i]);
            list.add(item);
        }
        talkBean.setAccountDesList(list);
        mTalkView=findViewById(R.id.hrzTalkView);
        mTalkView.setDatas(talkBean);
    }
}
