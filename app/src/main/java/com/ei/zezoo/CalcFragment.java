package com.ei.zezoo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fathzer.soft.javaluator.DoubleEvaluator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalcFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalcFragment extends Fragment implements XmlClickable {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    boolean isScience = false;
    Fragment fragment;
    private Context context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((UserActivity) getActivity()).someFragment = this;

    }

    public CalcFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalcFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalcFragment newInstance(String param1, String param2) {
        CalcFragment fragment = new CalcFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calc, container, false);
        context = view.getContext();
        fragment = new Calc1Fragment();
        some1Fragment = (Calc1Fragment) fragment;
        setHasOptionsMenu(true);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentMain1, fragment);
        ft.commit();
        isScience = false;
        return view;

    }

    void setCurrentFragment(Fragment f, int i) {
        fragment = f;
        if (i == 0)
            some1Fragment = (CalcHFragment) fragment;
        else if (i == 1)
            some1Fragment = (Calc1Fragment) fragment;
        else
            some1Fragment = (Calc2Fragment) fragment;

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentMain1, fragment);
        ft.commit();
    }

    Menu men;
    MenuItem menuCalc;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_calc, menu);
        men = menu;
        menuCalc = menu.findItem(R.id.menu_scien);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scien:
                if (!isScience) {
                    menuCalc.setTitle("الآلة الحاسبة");
                    isScience = true;
                    ((UserActivity) getActivity()).toolbar.setTitle("الآلة الحاسبة العلمية");
                    fragment = new Calc2Fragment();
                    some1Fragment = (Calc2Fragment) fragment;
                    FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    ft.replace(R.id.fragmentMain1, fragment);
                    ft.commit();
                } else {
                    menuCalc.setTitle("الآلة الحاسبة العلمية");
                    isScience = false;
                    ((UserActivity) getActivity()).toolbar.setTitle("الآلة الحاسبة");
                    fragment = new Calc1Fragment();
                    some1Fragment = (Calc1Fragment) fragment;
                    FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    ft.replace(R.id.fragmentMain1, fragment);
                    ft.commit();
                }
                return true;
            case R.id.menu_unitl:
                ((UserActivity) getActivity()).toolbar.setTitle("تحويلات واحدة المسافة");
                fragment = new Calc3Fragment();
                some1Fragment = (Calc3Fragment) fragment;
                FragmentTransaction ft1 = getChildFragmentManager().beginTransaction();
                ft1.replace(R.id.fragmentMain1, fragment);
                ft1.commit();
                return true;
            case R.id.menu_units:
                ((UserActivity) getActivity()).toolbar.setTitle("تحويلات واحدة المساحة");
                fragment = new Calc4Fragment();
                some1Fragment = (Calc4Fragment) fragment;
                FragmentTransaction ft2 = getChildFragmentManager().beginTransaction();
                ft2.replace(R.id.fragmentMain1, fragment);
                ft2.commit();
                return true;
            case R.id.menu_unitw:
                ((UserActivity) getActivity()).toolbar.setTitle("تحويلات واحدة الكتلة");
                fragment = new Calc5Fragment();
                some1Fragment = (Calc5Fragment) fragment;
                FragmentTransaction ft3 = getChildFragmentManager().beginTransaction();
                ft3.replace(R.id.fragmentMain1, fragment);
                ft3.commit();
                return true;
            case R.id.menu_unitt:
                ((UserActivity) getActivity()).toolbar.setTitle("تحويلات واحدة درجة الحرارة");
                fragment = new Calc6Fragment();
                some1Fragment = (Calc6Fragment) fragment;
                FragmentTransaction ft4 = getChildFragmentManager().beginTransaction();
                ft4.replace(R.id.fragmentMain1, fragment);
                ft4.commit();
                return true;
            case R.id.menu_u_about:

                return true;
            case R.id.menu_u_exit:
                ((UserActivity) getActivity()).closeApplication();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    XmlClickable some1Fragment;

    @Override
    public void onClick(View v) {
        some1Fragment.onClick(v);
    }
}