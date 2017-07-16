create table `user` (
`id` int unsigned primary key auto_increment,
`name` varchar(16) unique not null default '',
`create_time` int unsigned not null
);
--;;
create table `role` (
`id` int unsigned primary key auto_increment,
`name` varchar(16) not null default '',
`parent_id` int unsigned not null default 0,
`create_time` int unsigned not null
);
--;;
create table `node` (
`id` int unsigned primary key auto_increment,
`name` varchar(16) not null default '',
`parent_id` int unsigned not null default 0,
`create_time` int unsigned not null
);
--;;
create table `user_role` (
`user_id` int unsigned not null,
`role_id` int unsigned not null,
`create_time` int unsigned not null,
primary key (user_id, role_id)
);
--;;
create table `role_node` (
`role_id` int unsigned not null,
`node_id` int unsigned not null,
`create_time` int unsigned not null,
primary key (role_id, node_id)
);
