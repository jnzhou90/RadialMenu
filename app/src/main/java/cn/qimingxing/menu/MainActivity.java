package cn.qimingxing.menu;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.radialmenu.OnRadailMenuClick;
import com.example.radialmenu.RadialMenuRenderer;
import com.example.radialmenu.RadialMenuView;

import java.util.ArrayList;



public class MainActivity extends FragmentActivity {

    //Variable declarations
    private RadialMenuRenderer mRenderer;
    private FrameLayout mHolderLayout;

    private String[] mMenuNames = {"1", "2", "3", "4"};
    private int[] mMenuImages = {R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher, R.drawable.ic_launcher};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_holder);


        //Init the frame layout
        mHolderLayout = findViewById(R.id.fragment_container);
        // Init the Radial Menu and menu items
        mRenderer = new RadialMenuRenderer(mHolderLayout, true, 100, 350, false);
        mRenderer.setRadialMenuContent(mMenuNames, mMenuImages);
        RadialMenuView view = mRenderer.renderView();
        mHolderLayout.addView(view);
        //Handle the menu item interactions
        view.setOnRadialMenuClickListener(new OnRadailMenuClick() {
            @Override
            public void onRadailMenuClickedListener(String id) {
                switch (id) {
                    case "1":
                        Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();
                        break;
                    case "2":
                        Toast.makeText(MainActivity.this, "2", Toast.LENGTH_SHORT).show();

                        break;
                    case "3":
                        break;
                    case "4":
                        break;
                    case "5":
                        break;
                    case "6":
                        break;
                    case "7":
                        break;
                    case "8":
                        break;
                    case "9":
                        break;
                    case "0":
                        break;
                    case "保存":
                        break;
                    case "退出":
                        break;

                }
            }

            @Override
            public void onRadailMenuActionDownListener(String id) {

            }
        });


    }


}
