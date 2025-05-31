insert into permiso values (1, 'CREATE'), (2, 'READ'), (3, 'UPDATE'), (4, 'DELETE');
insert into rol (rol) values ('USUARIO'), ('ADMINISTRADOR'), ('ANALISTA');
insert into rol_permiso (id_rol, id_permiso) values (1, 1), (1, 2), (1, 3), (1, 4), (2, 1), (2, 2), (2, 3), (2, 4), (3, 1);