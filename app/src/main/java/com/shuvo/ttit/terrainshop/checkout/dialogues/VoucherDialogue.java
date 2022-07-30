package com.shuvo.ttit.terrainshop.checkout.dialogues;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.checkout.CheckOut;

import static com.shuvo.ttit.terrainshop.checkout.CheckOut.voucherAmount;
import static com.shuvo.ttit.terrainshop.checkout.CheckOut.voucherCode;

import java.util.Objects;

public class VoucherDialogue extends AppCompatDialogFragment {

    TextInputEditText voucherText;
    TextInputLayout voucherLay;
    MaterialButton apply;
    ImageView close;

    AlertDialog dialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.voucher_item_layout, null);

        voucherText = view.findViewById(R.id.voucher_given_voucher_generate);
        voucherLay = view.findViewById(R.id.voucher_text_layout_voucher_generate);

        apply = view.findViewById(R.id.voucher_apply_button_voucher_generate);
        close = view.findViewById(R.id.close_logo_voucher_generate);


        builder.setView(view);

        dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        voucherText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                voucherLay.setHelperText("");
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  text = Objects.requireNonNull(voucherText.getText()).toString();
                if (!text.isEmpty()) {
                    if (text.equals("OFFER30")) {
                        voucherAmount = 30;
                        voucherCode = "OFFER30";
                        CheckOut.VoucherCheck();
                        dialog.dismiss();
                    }
                    else {
                        voucherLay.setHelperText("Wrong voucher code.");
                    }
                } else {
                    voucherLay.setHelperText("Please give your voucher code first.");
                }
            }
        });

        return dialog;
    }
}
