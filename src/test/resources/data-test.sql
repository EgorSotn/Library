insert into authors ( name_author, year_author )
values ( 'Pushkin', '1993-05-11');
insert into genres ( name_genre)
values ( 'drama');
insert into genres ( name_genre)
values ( 'horror');

insert into books ( name_book, year_book,  id_author)
values('evangelion', '1999-05-11', 1);
insert into books (name_book, year_book,  id_author)
values ('eva', '1999-05-10', 1);

insert into book_genre (id_book, id_genre)
values (1, 1);
insert into book_genre (id_book, id_genre)
values (1, 2);

insert into book_genre (id_book, id_genre)
values (2, 1);

insert into comments( text, id_book)
values('adscxvcxvxvg', 1)