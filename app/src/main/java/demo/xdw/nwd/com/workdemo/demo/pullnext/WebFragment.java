package demo.xdw.nwd.com.workdemo.demo.pullnext;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import demo.xdw.nwd.com.workdemo.R;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class WebFragment extends Fragment {


    private WebView webView;


    private int index;

    public WebFragment() {

    }


    public static WebFragment newInstant(int index) {
        WebFragment webViewFragment = new WebFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        webViewFragment.setArguments(bundle);
        return webViewFragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        index = getArguments().getInt("index");

        View v = inflater.inflate(R.layout.fragment_web_view2, container, false);

        return v;
    }


}
