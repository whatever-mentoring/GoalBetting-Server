create table if not exists gifticon
(
    id           bigint auto_increment primary key,
    user_id      bigint       not null,
    url          varchar(255) not null,
    is_validated bit          not null,
    deleted_at   datetime(6)  null,
    created_at   datetime(6)  not null,
    updated_at   datetime(6)  not null
);

create table if not exists goal
(
    id         bigint auto_increment primary key,
    user_id    bigint                                 not null,
    content    varchar(255)                           not null,
    type       enum ('FREE', 'BILLING')               not null,
    threshold  int                                    not null,
    result     enum ('PROCEEDING', 'SUCCESS', 'FAIL') not null,
    start_date datetime(6)                            not null,
    end_date   datetime(6)                            not null,
    deleted_at datetime(6)                            null,
    created_at datetime(6)                            not null,
    updated_at datetime(6)                            not null
);

create table if not exists goal_gifticon
(
    id          bigint auto_increment primary key,
    goal_id     bigint      not null,
    gifticon_id bigint      not null,
    deleted_at  datetime(6) null,
    created_at  datetime(6) not null,
    updated_at  datetime(6) not null
);

create table if not exists refresh_token
(
    id         bigint auto_increment primary key,
    user_id    bigint       not null,
    payload    varchar(255) null,
    deleted_at datetime(6)  null,
    created_at datetime(6)  not null,
    updated_at datetime(6)  not null
);

create table if not exists user
(
    id                  bigint auto_increment primary key,
    nickname            varchar(255) not null,
    fcm_token_payload   varchar(255) null,
    oauth_token_payload varchar(255) null,
    deleted_at          datetime(6)  null,
    created_at          datetime(6)  not null,
    updated_at          datetime(6)  not null
);

create table if not exists betting
(
    id              bigint auto_increment primary key,
    goal_id         bigint                                 not null,
    user_id         bigint                                 not null,
    prediction_type enum ('SUCCESS', 'FAIL')               not null,
    result          enum ('PROCEEDING', 'FAIL', 'GET_GIFTICON', 'NO_GIFTICON') not null,
    deleted_at      datetime(6)                            null,
    created_at      datetime(6)                            not null,
    updated_at      datetime(6)                            not null
);

create table if not exists goal_cheering
(
    id               bigint auto_increment primary key,
    goal_id          bigint       not null,
    user_id          bigint       not null,
    cheering_message varchar(255) not null,
    deleted_at       datetime(6)  null,
    created_at       datetime(6)  not null,
    updated_at       datetime(6)  not null
);

create table if not exists goal_proof
(
    id         bigint auto_increment primary key,
    goal_id    bigint       not null,
    user_id    bigint       not null,
    url        varchar(255) not null,
    comment    varchar(255) not null,
    deleted_at datetime(6)  null,
    created_at datetime(6)  not null,
    updated_at datetime(6)  not null
);