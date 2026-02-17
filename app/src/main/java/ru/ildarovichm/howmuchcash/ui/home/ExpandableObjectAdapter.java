package ru.ildarovichm.howmuchcash.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import ru.ildarovichm.howmuchcash.ObjectGroup;
import ru.ildarovichm.howmuchcash.ObjectUnit;
import ru.ildarovichm.howmuchcash.R;
import ru.ildarovichm.howmuchcash.ui.home.ExpandableObjectAdapter;

public class ExpandableObjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<Object> flatList = new ArrayList<>();
    private Map<String, ObjectGroup> groups = new HashMap<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onObjectClick(ObjectUnit object);
    }

    public ExpandableObjectAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(ArrayList<ObjectUnit> objects, ObjectGroup.GroupLevel groupLevel) {
        groups.clear();
        flatList.clear();

        // Группируем
        for (ObjectUnit obj : objects) {
            String key = ObjectGroup.getGroupKey(obj, groupLevel);
            groups.computeIfAbsent(key, k -> new ObjectGroup(obj.getUnit().getCity(), obj.getUnit().getStreet(), obj.getUnit().getBuilding()))
                    .getObjects().add(obj);
        }

        // Формируем плоский список
        for (ObjectGroup group : groups.values()) {
            flatList.add(group);
            if (group.isExpanded()) {
                flatList.addAll(group.getObjects());
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return flatList.get(position) instanceof ObjectGroup ? TYPE_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_group_header, parent, false);
            return new GroupViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_object_card, parent, false);
            return new ObjectViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GroupViewHolder) {
            GroupViewHolder vh = (GroupViewHolder) holder;
            ObjectGroup group = (ObjectGroup) flatList.get(position);
            vh.bind(group);
        } else {
            ObjectViewHolder vh = (ObjectViewHolder) holder;
            ObjectUnit object = (ObjectUnit) flatList.get(position);
            vh.bind(object);
        }
    }

    @Override
    public int getItemCount() {
        return flatList.size();
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView tvGroupName;
        ImageView ivArrow;

        GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGroupName = itemView.findViewById(R.id.tv_group_name);
            ivArrow = itemView.findViewById(R.id.iv_arrow);
        }

        void bind(ObjectGroup group) {
            String name = group.getCity() + ", " + group.getStreet();
            if (group.getBuilding() != null && !group.getBuilding().isEmpty()) {
                name += ", д. " + group.getBuilding();
            }
            tvGroupName.setText(name);

            boolean isExpanded = group.isExpanded();
            ivArrow.setRotation(isExpanded ? 90 : 0);

            itemView.setOnClickListener(v -> {
                group.setExpanded(!group.isExpanded());
                refreshFlatList();
            });
        }
    }

    class ObjectViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddress;
        CheckBox cbTO;

        ObjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tv_address);
            cbTO = itemView.findViewById(R.id.cb_to);
        }

        void bind(ObjectUnit object) {
            tvAddress.setText(object.getUnit().getAddress());
            cbTO.setChecked(object.getTOCheckBoxState());

            cbTO.setOnCheckedChangeListener((btn, isChecked) -> {
                object.setTOCheckBoxState(isChecked);
                if (listener != null) listener.onObjectClick(object);
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onObjectClick(object);
            });
        }
    }

    private void refreshFlatList() {
        flatList.clear();
        for (ObjectGroup group : groups.values()) {
            flatList.add(group);
            if (group.isExpanded()) {
                flatList.addAll(group.getObjects());
            }
        }
        notifyDataSetChanged();
    }
}