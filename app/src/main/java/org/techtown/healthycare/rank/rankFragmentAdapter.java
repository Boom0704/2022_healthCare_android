package org.techtown.healthycare.rank;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class rankFragmentAdapter  extends FragmentStateAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>();

    public rankFragmentAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    // 어댑터에 프래그먼트 추가
    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
        notifyItemInserted(fragments.size() - 1); // 추가된 사이즈
    }
}