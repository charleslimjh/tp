package eatwhere.foodguide.logic.commands;

import static eatwhere.foodguide.logic.parser.CliSyntax.PREFIX_TAG;
import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eatwhere.foodguide.commons.core.Messages;
import eatwhere.foodguide.commons.core.index.Index;
import eatwhere.foodguide.logic.commands.exceptions.CommandException;
import eatwhere.foodguide.model.Model;
import eatwhere.foodguide.model.eatery.Eatery;
import eatwhere.foodguide.model.eatery.Cuisine;
import eatwhere.foodguide.model.eatery.Location;
import eatwhere.foodguide.model.eatery.Name;
import eatwhere.foodguide.model.eatery.Phone;
import eatwhere.foodguide.model.tag.Tag;

/**
 * Adds tags to an existing eatery in the food guide.
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds tags to the eatery identified "
            + "by the index number used in the displayed eatery list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_TAG + "halal";

    public static final String MESSAGE_TAG_EATERY_SUCCESS = "Tagged Eatery: %1$s";
    public static final String MESSAGE_NOT_TAGGED = "At least one tag must be provided.";
    public static final String MESSAGE_DUPLICATE_EATERY = "This eatery already exists in the food guide.";

    private final Index index;
    private Set<Tag> tagsToAdd;

    /**
     * @param index of the eatery in the filtered eatery list to tag
     * @param tagsToAdd tags to add to the eatery
     */
    public TagCommand(Index index, Set<Tag> tagsToAdd) {
        requireNonNull(index);
        requireNonNull(tagsToAdd);

        this.index = index;
        this.tagsToAdd = new HashSet<>(tagsToAdd);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Eatery> lastShownList = model.getFilteredEateryList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EATERY_DISPLAYED_INDEX);
        }

        Eatery eateryToTag = lastShownList.get(index.getZeroBased());
        Eatery taggedEatery = createTaggedEatery(eateryToTag, tagsToAdd);

        if (!eateryToTag.isSameEatery(taggedEatery) && model.hasEatery(taggedEatery)) {
            throw new CommandException(MESSAGE_DUPLICATE_EATERY);
        }

        model.setEatery(eateryToTag, taggedEatery);
        model.updateFilteredEateryList(Model.PREDICATE_SHOW_ALL_EATERIES);
        return new CommandResult(String.format(MESSAGE_TAG_EATERY_SUCCESS, taggedEatery));
    }

    /**
     * Creates and returns an {@code Eatery} that's a copy of {@code eateryToTag}
     * with {@code tagsToAdd} included.
     */
    private static Eatery createTaggedEatery(Eatery eateryToTag, Set<Tag> tagsToAdd) {
        assert eateryToTag != null;

        Name name = eateryToTag.getName();
        Phone phone = eateryToTag.getPhone();
        Cuisine cuisine = eateryToTag.getCuisine();
        Location location = eateryToTag.getLocation();
        Set<Tag> tags = new HashSet<>(eateryToTag.getTags()); //hashset supports addAll()
        tags.addAll(tagsToAdd);

        return new Eatery(name, phone, cuisine, location, tags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagCommand)) {
            return false;
        }

        // state check
        TagCommand e = (TagCommand) other;
        return index.equals(e.index)
                && tagsToAdd.equals(e.tagsToAdd);
    }
}
