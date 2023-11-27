create table gifticon
(
    id           bigint auto_increment primary key,
    url          varchar(255) not null,
    is_validated bit          not null,
    is_deleted   bit          not null,
    created_at   datetime(6)  not null,
    updated_at   datetime(6)  not null
);

create table goal
(
    id         bigint auto_increment primary key,
    content    varchar(255)             not null,
    type       enum ('FREE', 'BILLING') not null,
    threshold  int                      not null,
    deadline   datetime(6)              not null,
    is_deleted bit                      not null,
    created_at datetime(6)              not null,
    updated_at datetime(6)              not null
);

create table goal_gifticon
(
    id          bigint auto_increment primary key,
    goal_id     bigint      not null,
    gifticon_id bigint      not null,
    is_deleted  bit         not null,
    created_at  datetime(6) not null,
    updated_at  datetime(6) not null
);

create table refresh_token
(
    id         bigint auto_increment primary key,
    payload    varchar(255) null,
    is_deleted bit          not null,
    created_at datetime(6)  not null,
    updated_at datetime(6)  not null
);

create table user
(
    id                  bigint auto_increment primary key,
    nickname            varchar(255) not null,
    fcm_token_payload   varchar(255) null,
    oauth_token_payload varchar(255) null,
    is_deleted          bit          not null,
    created_at          datetime(6)  not null,
    updated_at          datetime(6)  not null
);

create table betting
(
    id              bigint auto_increment primary key,
    goal_id         bigint                                 not null,
    user_id         bigint                                 not null,
    prediction_type enum ('SUCCESS', 'FAIL')               not null,
    result          enum ('PROCEEDING', 'SUCCESS', 'FAIL') not null,
    is_deleted      bit                                    not null,
    created_at      datetime(6)                            not null,
    updated_at      datetime(6)                            not null
);

create table goal_cheering
(
    id               bigint auto_increment primary key,
    goal_id          bigint       not null,
    user_id          bigint       not null,
    cheering_message varchar(255) not null,
    is_deleted       bit          not null,
    created_at       datetime(6)  not null,
    updated_at       datetime(6)  not null
);

create table goal_proof
(
    id         bigint auto_increment primary key,
    goal_id    bigint       not null,
    user_id    bigint       not null,
    document   varchar(255) not null,
    is_deleted bit          not null,
    created_at datetime(6)  not null,
    updated_at datetime(6)  not null
);

create table user_refresh_token
(
    id               bigint auto_increment primary key,
    user_id          bigint      not null,
    refresh_token_id bigint      not null,
    is_deleted       bit         not null,
    created_at       datetime(6) not null,
    updated_at       datetime(6) not null
);