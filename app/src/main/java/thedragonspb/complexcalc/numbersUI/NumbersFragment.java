package thedragonspb.complexcalc.numbersUI;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import thedragonspb.complexcalc.Data;
import thedragonspb.complexcalc.R;
import thedragonspb.complexcalc.calc.ComplexNumber;

/**
 * Created by thedragonspb on 29.01.17.
 */
public class NumbersFragment extends Fragment {

    RecyclerView recyclerView;
    NumberAdapter adapter;
    FloatingActionButton fab;

    List<ComplexNumber> numbers;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_fragment, container, false);

        numbers = Data.getInstance().getSavedNumbers();

        recyclerView = (RecyclerView) v.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NumberAdapter();
        recyclerView.setAdapter(adapter);

        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddNumberDialog().show(getFragmentManager(), "ADD NUMBER DIALOG");
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isShown()) {
                    fab.hide();
                }
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        return v;
    }


    private class NumberHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView polarForm;
        TextView expForm;
        ImageButton copyBtn;
        ImageButton deleteBtn;

        ComplexNumber number;

        public NumberHolder(View itemView) {
            super(itemView);
            name       = (TextView) itemView.findViewById(R.id.name);
            polarForm  = (TextView) itemView.findViewById(R.id.polarForm);
            expForm    = (TextView) itemView.findViewById(R.id.expForm);
            copyBtn    = (ImageButton) itemView.findViewById(R.id.copyBTN);
            deleteBtn  = (ImageButton) itemView.findViewById(R.id.deleteBTN);

            copyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Data.getInstance().setBuffer(number);
                    Toast.makeText(getActivity(), R.string.copied, Toast.LENGTH_SHORT).show();
                }
            });
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.deleteNumber)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Data.getInstance().deleteNumber(number);
                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    builder.create().show();
                }
            });
        }

        public void bindData(ComplexNumber number) {
            this.number = number;
            polarForm.setText("" + number.getPolarForm().toString(Data.getInstance().getRounding()));
            expForm.setText("" + number.getExpForm().toString(Data.getInstance().getRounding()));
        }
    }

    private class NumberAdapter extends RecyclerView.Adapter<NumberHolder> {

        public NumberAdapter() {
        }

        @Override
        public NumberHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.list_item, parent, false);
            return new NumberHolder(v);
        }

        @Override
        public void onBindViewHolder(NumberHolder holder, int position) {
            holder.bindData(numbers.get(position));
            holder.name.setText(numbers.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return numbers.size();
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (this != null) {
            if (isVisibleToUser == true) {
                if (numbers.size() != adapter.getItemCount() + 1) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}