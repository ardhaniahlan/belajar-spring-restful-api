CREATE TABLE users (
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    token VARCHAR(100),
    token_expired_at BIGINT,
    PRIMARY KEY (username),
    UNIQUE (token)
) ENGINE InnoDB;

select * from users;
desc users;

CREATE TABLE contacts(
    id VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL,
    firstname VARCHAR(100) NOT NULL,
    lastname VARCHAR(100),
    phone VARCHAR(100),
    email VARCHAR(100),
    primary key (id),
    foreign key fk_users_contact (username) references users (username)
) ENGINE InnoDB;
select * from contacts;
desc contacts;

create table addresses(
    id VARCHAR(100) NOT NULL,
    contact_id VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    street VARCHAR(100),
    city VARCHAR(100),
    province VARCHAR(100),
    portal_code VARCHAR(10),
    primary key (id),
    foreign key fk_contacts_addresses (contact_id) references contacts (id)
) ENGINE InnoDB;

select * from addresses;
desc addresses;

ALTER TABLE addresses RENAME COLUMN portal_code TO postal_code;
