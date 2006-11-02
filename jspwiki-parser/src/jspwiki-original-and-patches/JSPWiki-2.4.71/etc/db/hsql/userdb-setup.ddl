drop table @jspwiki.userdatabase.table@ if exists;
drop table @jspwiki.userdatabase.roleTable@ if exists;
drop table @jspwiki.groupdatabase.table@ if exists;
drop table @jspwiki.groupdatabase.membertable@ if exists;
drop user @jdbc.user.id@;

create table @jspwiki.userdatabase.table@ (
  @jspwiki.userdatabase.email@ varchar(100),
  @jspwiki.userdatabase.fullName@ varchar(100),
  @jspwiki.userdatabase.loginName@ varchar(100) not null,
  @jspwiki.userdatabase.password@ varchar(100),
  @jspwiki.userdatabase.wikiName@ varchar(100),
  @jspwiki.userdatabase.created@ timestamp,
  @jspwiki.userdatabase.modified@ timestamp,
  constraint @jspwiki.userdatabase.table@ primary key (@jspwiki.userdatabase.loginName@)
);

create table @jspwiki.userdatabase.roleTable@ (
  @jspwiki.userdatabase.loginName@ varchar(100) not null,
  @jspwiki.userdatabase.role@ varchar(100) not null
);

create table @jspwiki.groupdatabase.table@ (
  @jspwiki.groupdatabase.name@ varchar(100) not null,
  @jspwiki.groupdatabase.creator@ varchar(100),
  @jspwiki.groupdatabase.created@ timestamp,
  @jspwiki.groupdatabase.modifier@ varchar(100),
  @jspwiki.groupdatabase.modified@ timestamp,
  constraint @jspwiki.groupdatabase.table@ primary key (@jspwiki.groupdatabase.name@)
);

create table @jspwiki.groupdatabase.membertable@ (
  @jspwiki.groupdatabase.name@ varchar(100) not null,
  @jspwiki.groupdatabase.member@ varchar(100) not null,
  constraint @jspwiki.groupdatabase.membertable@ primary key
    (@jspwiki.groupdatabase.name@,@jspwiki.groupdatabase.member@)
);

create user @jdbc.user.id@ password "@jdbc.user.password@";

grant select, insert, update, delete on @jspwiki.userdatabase.table@ to @jdbc.user.id@;
grant select, insert, update, delete on @jspwiki.userdatabase.roleTable@ to @jdbc.user.id@;
grant select, insert, update, delete on @jspwiki.groupdatabase.table@ to @jdbc.user.id@;
grant select, insert, update, delete on @jspwiki.groupdatabase.membertable@ to @jdbc.user.id@;

insert into @jspwiki.userdatabase.table@ (
  @jspwiki.userdatabase.email@,
  @jspwiki.userdatabase.fullName@,
  @jspwiki.userdatabase.loginName@,
  @jspwiki.userdatabase.password@,
  @jspwiki.userdatabase.wikiName@
) values (
  'janne@ecyrd.com',
  'Janne Jalkanen',
  'janne',
  '457b08e825da547c3b77fbc1ff906a1d00a7daee',
  'JanneJalkanen'
);
  
insert into @jspwiki.userdatabase.roleTable@ (
  @jspwiki.userdatabase.loginName@,
  @jspwiki.userdatabase.role@
) values (  
  'janne',
  'Authenticated'
);

insert into @jspwiki.groupdatabase.table@ (
  @jspwiki.groupdatabase.name@,
  @jspwiki.groupdatabase.created@,
  @jspwiki.groupdatabase.modified@
) values (
  'TV',
  '2006-06-20 14:50:54.00000000',
  '2006-06-20 14:50:54.00000000'
);
insert into @jspwiki.groupdatabase.membertable@ (
  @jspwiki.groupdatabase.name@,
  @jspwiki.groupdatabase.member@
) values (  
  'TV',
  'Archie Bunker'
);
insert into @jspwiki.groupdatabase.membertable@ (
  @jspwiki.groupdatabase.name@,
  @jspwiki.groupdatabase.member@
) values (  
  'TV',
  'BullwinkleMoose'
);
insert into @jspwiki.groupdatabase.membertable@ (
  @jspwiki.groupdatabase.name@,
  @jspwiki.groupdatabase.member@
) values (  
  'TV',
  'Fred Friendly'
);

insert into @jspwiki.groupdatabase.table@ (
  @jspwiki.groupdatabase.name@,
  @jspwiki.groupdatabase.created@,
  @jspwiki.groupdatabase.modified@
) values (
  'Literature',
  '2006-06-20 14:50:54.00000000',
  '2006-06-20 14:50:54.00000000'
);
insert into @jspwiki.groupdatabase.membertable@ (
  @jspwiki.groupdatabase.name@,
  @jspwiki.groupdatabase.member@
) values (  
  'Literature',
  'Charles Dickens'
);
insert into @jspwiki.groupdatabase.membertable@ (
  @jspwiki.groupdatabase.name@,
  @jspwiki.groupdatabase.member@
) values (  
  'Literature',
  'Homer'
);

insert into @jspwiki.groupdatabase.table@ (
  @jspwiki.groupdatabase.name@,
  @jspwiki.groupdatabase.created@,
  @jspwiki.groupdatabase.modified@
) values (
  'Art',
  '2006-06-20 14:50:54.00000000',
  '2006-06-20 14:50:54.00000000'
);

insert into @jspwiki.groupdatabase.table@ (
  @jspwiki.groupdatabase.name@,
  @jspwiki.groupdatabase.created@,
  @jspwiki.groupdatabase.modified@
) values (
  'Admin',
  '2006-06-20 14:50:54.00000000',
  '2006-06-20 14:50:54.00000000'
);
insert into @jspwiki.groupdatabase.membertable@ (
  @jspwiki.groupdatabase.name@,
  @jspwiki.groupdatabase.member@
) values (  
  'Admin',
  'Administrator'
);
