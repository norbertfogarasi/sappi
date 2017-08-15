package com.lynxsolutions.intern.sappi.cars;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.lynxsolutions.intern.sappi.R;

import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.location.places.ui.PlaceAutocomplete.MODE_OVERLAY;
import static com.google.android.gms.location.places.ui.PlaceAutocomplete.RESULT_ERROR;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomFragment extends Fragment {

    public static final int REQUEST_CODE = 1;
    private View view;
    private CharSequence hint;
    private PlaceSelectionListener listener;
    private TextView textView;
    private ImageView imageView;
    private int imageResource;

    public CustomFragment() {
        // Required empty public constructor
    }

    public void setHint(CharSequence hint){
        this.hint = hint;
    }

    public void  setText(CharSequence text){
        textView.setText(text);
    }

    public void setIcon(int imageResource){
        this.imageResource = imageResource;
    }

    public void setOnPlaceSelectedListener(PlaceSelectionListener listener){
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_custom, container, false);

        imageView = view.findViewById(R.id.icon_image);
        if(imageResource != 0)
            imageView.setImageResource(imageResource);

        textView = view.findViewById(R.id.search_tv);
        textView.setHint(hint);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                        .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                        .setCountry("RO")
                        .build();

                Intent intent = null;
                try {
                    intent = new PlaceAutocomplete.IntentBuilder(MODE_OVERLAY)
                            .setFilter(typeFilter)
                            .build(getActivity());
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        return view;
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                setText(place.getName());
                if(listener != null)
                    listener.onPlaceSelected(place);
            }
            else if(resultCode == RESULT_ERROR){
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                if(listener != null)
                    listener.onError(status);
            }
        }
    }

}
