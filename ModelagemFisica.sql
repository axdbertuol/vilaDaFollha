create table inhabitants
(
    id        bigserial primary key,
    name      varchar(100),
    last_name varchar(200),
    cpf       varchar(11) unique,
    email     varchar(150) unique,
    password  varchar(255),
    birthday  date,
    balance   dec(10, 2),
    roles     varchar(50)[]
);

insert into inhabitants(name, last_name, cpf, email, password, birthday, balance, roles)
values ('johnny', 'my man', '00434587756', 'johnny@test.com', '$2a$10$O/Jtn9tPS3KMajysITf4P.1fmgBxAN4wv0pj8/M.4TJZv8dfC/hmK', '2010-09-01', 5003.45, ARRAY ['ROLE_USER']);

insert into inhabitants(name, last_name, cpf, email, password, birthday, balance, roles)
values ('greatr', 'ferro', '00134587716', 'g@test.com', '$2a$10$O/Jtn9tPS3KMajysITf4P.1fmgBxAN4wv0pj8/M.4TJZv8dfC/hmK', '2000-04-06', 9902.45, ARRAY ['ROLE_USER']);
insert into inhabitants(name, last_name, cpf, email, password, birthday, balance, roles)
values ('hurray', 'xaaad', '00431587716', 'axd@test.com', '$2a$10$O/Jtn9tPS3KMajysITf4P.1fmgBxAN4wv0pj8/M.4TJZv8dfC/hmK', '2010-09-06', 99999.45,
        ARRAY ['ROLE_USER', 'ROLE_ADMIN']);
insert into inhabitants(name, last_name, cpf, email, password, birthday, balance, roles)
values ('xis', 'my x', '00434587716', 'x@test.com', '$2a$10$O/Jtn9tPS3KMajysITf4P.1fmgBxAN4wv0pj8/M.4TJZv8dfC/hmK', '2010-09-06', 5201.45, ARRAY ['ROLE_USER']);

