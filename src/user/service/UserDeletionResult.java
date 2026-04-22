package user.service;

/**
 * Named outcomes for user-deletion requests exposed to the GUI layer.
 */
public enum UserDeletionResult {
    NOT_AUTHENTICATED,
    DELETED_BY_ADMIN,
    DELETED_SELF,
    NOT_PERMITTED;

    /**
     * Indicates whether the deletion request completed successfully.
     *
     * @return {@code true} when the target user was deleted
     */
    public boolean isSuccess() {
        return this == DELETED_BY_ADMIN || this == DELETED_SELF;
    }
}
