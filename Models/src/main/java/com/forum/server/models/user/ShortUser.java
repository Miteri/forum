package com.forum.server.models.user;

/**
 * 15.08.16
 *
 * @author Dinar Rafikov (First Software Engineering Platform)
 * @version 1.0
 */
public class ShortUser {
    private long userId;
    private String nickname;
    private Long rating;
    private String rights;

    public String getRights() {
        return rights;
    }

    private String avatar;

    public long getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public Long getRating() {
        return rating;
    }

    public String getAvatar() {
        return avatar;
    }


    protected ShortUser() {}

    protected ShortUser(Builder builder) {
        this.userId = builder.userId;
        this.nickname = builder.nickname;
        this.rating = builder.rating;
        this.avatar = builder.avatar;
        this.rights = builder.rights;
    }

    public static class Builder {
        private long userId;
        private String nickname;
        private Long rating;
        private String avatar;
        private String rights;

        public Builder Rights(String rights) {
            this.rights = rights;
            return this;
        }

        public Builder UserId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder Nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder Rating(Long rating) {
            this.rating = rating;
            return this;
        }

        public Builder Avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public  ShortUser build() {
            return new ShortUser(this);
        }
    }
}
