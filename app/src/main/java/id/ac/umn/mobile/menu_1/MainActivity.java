package id.ac.umn.mobile.menu_1;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;


public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_school_white_48dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_person_white_48dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_poll_white_48dp));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        pAdapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pAdapter);
        toolbar.setTitle("Same Page");
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        viewPager.setCurrentItem(0);
                        toolbar.setTitle("Tutorial");
                        break;
                    case 1:
                        viewPager.setCurrentItem(1);
                        toolbar.setTitle("Profile");
                        break;
                    case 2:
                        viewPager.setCurrentItem(2);
                        toolbar.setTitle("Rank");
                        break;
                    default:
                        viewPager.setCurrentItem(tab.getPosition());
                        toolbar.setTitle("Tutorial");
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    OneFragment tab0 = new OneFragment();
                    return tab0;
                case 1:
                    TwoFragment tab1 = new TwoFragment();
                    return tab1;
                case 2:
                    ThreeFragment tab2 = new ThreeFragment();
                    return tab2;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_side,menu);
        return true;
    }
}
