package ru.ildarovichm.howmuchcash;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ObjectUnitAdapter extends RecyclerView.Adapter<ObjectUnitAdapter.ObjectUnitViewHolder> {

    private static ArrayList<ObjectUnit> objectUnitList;
    private static OnItemClickListener listener;
    private static OnItemActionListener actionListener;

    public ArrayList<ObjectUnit> getObjectUnitList() {
        return objectUnitList;
    }

    // Интерфейс для кликов по карточке
    public interface OnItemClickListener {
        void onItemClick(ObjectUnit objectUnit);
    }

    // Интерфейс для действий с кнопок
    public interface OnItemActionListener {
        void onEditClick(ObjectUnit objectUnit, int position);
        void onDeleteClick(ObjectUnit objectUnit, int position);
        void onDetailsClick(ObjectUnit objectUnit, int position);
    }

    // Конструктор
    public ObjectUnitAdapter(ArrayList<ObjectUnit> objectUnitList) {
        ObjectUnitAdapter.objectUnitList = objectUnitList;
    }

    public void updateList(ArrayList<ObjectUnit> newList) {
        objectUnitList = newList;
        notifyDataSetChanged(); // Уведомляем адаптер, что данные изменились
    }

    // Сеттеры для слушателей
    public void setOnItemClickListener(OnItemClickListener listener) {
        ObjectUnitAdapter.listener = listener;
    }

    public void setOnItemActionListener(OnItemActionListener actionListener) {
        ObjectUnitAdapter.actionListener = actionListener;
    }

    @NonNull
    @Override
    public ObjectUnitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_objectunit, parent, false);
        return new ObjectUnitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObjectUnitViewHolder holder, int position) {
        ObjectUnit objectUnit = objectUnitList.get(position);
        holder.bind(objectUnit, position);
    }

    @Override
    public int getItemCount() {
        return objectUnitList.size();
    }

    // Метод для удаления элемента
    public void removeItem(int position) {
        objectUnitList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, objectUnitList.size());
    }

    // Метод для обновления элемента
    public void updateItem(int position, ObjectUnit newObjectUnit) {
        objectUnitList.set(position, newObjectUnit);
        notifyItemChanged(position);
    }

    // Метод для добавления элемента
    public void addItem(ObjectUnit objectUnit) {
        objectUnitList.add(objectUnit);
        notifyItemInserted(objectUnitList.size() - 1);
    }

    // ViewHolder класс
    static class ObjectUnitViewHolder extends RecyclerView.ViewHolder {
        private TextView textAddress, textTypeUnit;
        private TextView textTypeObject, textFloors, textParking;
        private TextView textToCheckBox;
        private Button btnEdit, btnDelete, btnDetails;

        public ObjectUnitViewHolder(@NonNull View itemView) {
            super(itemView);

            // Инициализация TextView
            textAddress = itemView.findViewById(R.id.textAddress);
            textTypeUnit = itemView.findViewById(R.id.textTypeUnit);
            textTypeObject = itemView.findViewById(R.id.textTypeObject);
            textFloors = itemView.findViewById(R.id.textFloors);
            textParking = itemView.findViewById(R.id.textParking);

            // Инициализация кнопок
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(ObjectUnit objectUnit, int position) {
            Unit unit = objectUnit.getUnit();

            // Устанавливаем значения
            if (unit != null) {
                textAddress.setText(unit.toString());
            } else {
                textAddress.setText("Адрес не указан");
            }

            textTypeUnit.setText(objectUnit.getTypeOfObjectUnit() != null ?
                    objectUnit.getTypeOfObjectUnit() : "Не указан");
            textTypeObject.setText(objectUnit.getTypeOfObject() != null ?
                    objectUnit.getTypeOfObject() : "Не указан");
            textFloors.setText(String.valueOf(objectUnit.getCountNumberOfFloorsOfObject()));

            // Парковка и чекбоксы с цветами
            setColoredText(textParking, objectUnit.getParkingAvailability());
            setColoredText(textToCheckBox, objectUnit.getTOCheckBoxState());

            // Обработка клика по карточке
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(objectUnit);
                }
            });

            // Обработка кнопок
            btnEdit.setOnClickListener(v -> {
                if (actionListener != null) {
                    actionListener.onEditClick(objectUnit, position);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (actionListener != null) {
                    actionListener.onDeleteClick(objectUnit, position);
                }
            });

            btnDetails.setOnClickListener(v -> {
                if (actionListener != null) {
                    actionListener.onDetailsClick(objectUnit, position);
                }
            });
        }

        private void setColoredText(TextView textView, boolean value) {
            String text = value ? "Да" : "Нет";
            textView.setText(text);
            if (value) {
                textView.setTextColor(itemView.getContext().getColor(android.R.color.holo_green_dark));
            } else {
                textView.setTextColor(itemView.getContext().getColor(android.R.color.holo_red_dark));
            }
        }
    }
}