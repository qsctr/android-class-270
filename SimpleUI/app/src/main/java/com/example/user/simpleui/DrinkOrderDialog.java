package com.example.user.simpleui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrinkOrderDialog.OnDrinkOrderListener} interface
 * to handle interaction events.
 * Use the {@link DrinkOrderDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrinkOrderDialog extends DialogFragment {

    private static final String ARG_PARAM1 = "param1";

    NumberPicker mediumNumberPicker;
    NumberPicker largeNumberPicker;

    RadioGroup iceRadioGroup;
    RadioGroup sugarRadioGroup;

    EditText noteEditText;

    private DrinkOrder drinkOrder;
    private OnDrinkOrderListener mListener;

    public DrinkOrderDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DrinkOrderDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static DrinkOrderDialog newInstance(DrinkOrder drinkOrder) {
        DrinkOrderDialog fragment = new DrinkOrderDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, drinkOrder.toData());
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_drink_order_dialog, container, false);
//    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            drinkOrder = DrinkOrder.newInstanceWithData(getArguments().getString(ARG_PARAM1));
            if (drinkOrder == null)
                throw new RuntimeException("Drink order instantiation failed");
        }

        View contentView = getActivity().getLayoutInflater()
                .inflate(R.layout.fragment_drink_order_dialog, null);

        mediumNumberPicker = (NumberPicker) contentView.findViewById(R.id.mediumNumberPicker);
        mediumNumberPicker.setMaxValue(100);
        mediumNumberPicker.setMinValue(0);
        mediumNumberPicker.setValue(drinkOrder.mNumber);

        largeNumberPicker = (NumberPicker) contentView.findViewById(R.id.largeNumberPicker);
        largeNumberPicker.setMaxValue(100);
        largeNumberPicker.setMinValue(0);
        largeNumberPicker.setValue(drinkOrder.lNumber);

        iceRadioGroup = (RadioGroup) contentView.findViewById(R.id.iceRadioGroup);

        sugarRadioGroup = (RadioGroup) contentView.findViewById(R.id.sugarRadioGroup);

        noteEditText = (EditText) contentView.findViewById(R.id.noteEditText);

        return new AlertDialog.Builder(getActivity())
                .setView(contentView)
                .setTitle(drinkOrder.drink.getName())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drinkOrder.mNumber = mediumNumberPicker.getValue();
                        drinkOrder.lNumber = largeNumberPicker.getValue();
                        drinkOrder.note = noteEditText.getText().toString();
                        drinkOrder.ice = getSelectedTextFromRadioGroup(iceRadioGroup);
                        drinkOrder.sugar = getSelectedTextFromRadioGroup(sugarRadioGroup);

                        if (mListener != null) {
                            mListener.onDrinkOrderFinished(drinkOrder);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }

    private String getSelectedTextFromRadioGroup(RadioGroup radioGroup) {
        return ((RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId()))
                .getText().toString();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDrinkOrderListener) {
            mListener = (OnDrinkOrderListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnDrinkOrderListener {
        void onDrinkOrderFinished(DrinkOrder drinkOrder);
    }
}
