package net.Indyuce.mmoitems.stat.data;

import com.google.gson.JsonArray;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import net.Indyuce.mmoitems.api.item.build.MMOItemBuilder;
import net.Indyuce.mmoitems.stat.data.random.RandomStatData;
import net.Indyuce.mmoitems.stat.data.type.Mergeable;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringListData implements StatData, RandomStatData<StringListData>, Mergeable<StringListData> {
    boolean removeGuarantee = false;
    @NotNull
    private List<String> list;

    public StringListData() {
        this(new ArrayList<>());
    }

    public StringListData(@NotNull String[] array) {
        this(Arrays.asList(array));
    }

    public StringListData(@NotNull JsonArray array) {
        this();

        array.forEach(str -> list.add(str.getAsString()));
    }

    public StringListData(@NotNull List<String> list) {
        this.list = list;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StringListData)) {
            return false;
        }
        if (((StringListData) obj).getList().size() != getList().size()) {
            return false;
        }
        return SilentNumbers.hasAll(((StringListData) obj).getList(), getList());
    }

    @NotNull
    public List<String> getList() {
        return list;
    }

    @Override
    public StringListData randomize(MMOItemBuilder builder) {
        return new StringListData(new ArrayList<>(list));
    }

    @Override
    public void merge(StringListData data) {
        list.addAll(data.list);
    }

    @Override
    @NotNull
    public StringListData cloneData() {
        return new StringListData(new ArrayList<>(getList()));
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public String toString() {

        StringBuilder b = new StringBuilder("\u00a77");
        for (String str : getList()) {
            if (b.length() > 0) {
                b.append("\u00a78;\u00a77 ");
            }
            b.append(str);
        }

        return b.toString();
    }

    /**
     * @param str Entry to remove
     * @return If the value was actually removed. If it wasn't there
     * in the first place, this will return false.
     * @deprecated Deprecated
     */
    @Deprecated
    public boolean remove(@Nullable String str) {

        if (!list.contains(str)) {
            return false;
        }

        if (removeGuarantee) {

            // Remove that sh
            return list.remove(str);
        } else {

            // OK
            try {
                return list.remove(str);

            } catch (UnsupportedOperationException ignored) {

                list = new ArrayList<>(list);
                removeGuarantee = true;
                return list.remove(str);
            }
        }
    }
}
