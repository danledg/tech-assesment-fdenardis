merge into PROGRAMME_ENTITY values('PGM001', true,  2000, 'My Title Here');
merge into PROGRAMME_ENTITY values('PGM002', true,  2000, 'My Title Here');
merge into PROGRAMME_ENTITY values('PGM003', true,  2000, 'My Title Here');
merge into PROGRAMME_ENTITY values('PGM004', true,  2000, 'My Title Here');
merge into PROGRAMME_ENTITY values('PGM005', true,  2000, 'My Title Here');
merge into PROGRAMME_ENTITY values('PGM006', true,  2000, 'My Title Here');
merge into PROGRAMME_ENTITY values('NODIS1', false,  2000, 'My Title Here');
merge into PROGRAMME_ENTITY values('NODIS2', false,  2000, 'My Title Here');
merge into PROGRAMME_ENTITY values('NODIS3', false,  2000, 'My Title Here');

merge into SCHEDULE_ENTRY_ENTITY(ID, START_DATE_TIME, END_DATE_TIME, PROGRAMME_ID) values(1001,'2021-08-01T13:00:00Z','2021-08-01T14:00:00Z', 'PGM001');
merge into SCHEDULE_ENTRY_ENTITY(ID, START_DATE_TIME, END_DATE_TIME, PROGRAMME_ID) values(1002,'2021-08-01T14:00:00Z','2021-08-01T15:00:00Z', 'PGM002');
merge into SCHEDULE_ENTRY_ENTITY(ID, START_DATE_TIME, END_DATE_TIME, PROGRAMME_ID) values(1003,'2021-08-01T15:00:00Z','2021-08-01T16:00:00Z', 'PGM003');
merge into SCHEDULE_ENTRY_ENTITY(ID, START_DATE_TIME, END_DATE_TIME, PROGRAMME_ID) values(1004,'2021-08-01T16:00:00Z','2021-08-01T18:00:00Z', 'NODIS1');
merge into SCHEDULE_ENTRY_ENTITY(ID, START_DATE_TIME, END_DATE_TIME, PROGRAMME_ID) values(1005,'2021-08-01T18:00:00Z','2021-08-01T20:00:00Z', 'PGM004');
merge into SCHEDULE_ENTRY_ENTITY(ID, START_DATE_TIME, END_DATE_TIME, PROGRAMME_ID) values(1006,'2021-08-01T20:00:00Z','2021-08-01T23:00:00Z', 'PGM005');

merge into SCHEDULE_ENTRY_ENTITY(ID, START_DATE_TIME, END_DATE_TIME, PROGRAMME_ID) values(1008,'2021-08-02T13:00:00Z','2021-08-02T14:00:00Z', 'PGM001');
merge into SCHEDULE_ENTRY_ENTITY(ID, START_DATE_TIME, END_DATE_TIME, PROGRAMME_ID) values(1009,'2021-08-02T14:00:00Z','2021-08-02T15:00:00Z', 'PGM002');
merge into SCHEDULE_ENTRY_ENTITY(ID, START_DATE_TIME, END_DATE_TIME, PROGRAMME_ID) values(1010,'2021-08-02T15:00:00Z','2021-08-02T16:00:00Z', 'PGM003');
merge into SCHEDULE_ENTRY_ENTITY(ID, START_DATE_TIME, END_DATE_TIME, PROGRAMME_ID) values(1011,'2021-08-02T16:00:00Z','2021-08-02T18:00:00Z', 'NODIS1');
merge into SCHEDULE_ENTRY_ENTITY(ID, START_DATE_TIME, END_DATE_TIME, PROGRAMME_ID) values(1012,'2021-08-02T18:00:00Z','2021-08-02T20:00:00Z', 'PGM004');
merge into SCHEDULE_ENTRY_ENTITY(ID, START_DATE_TIME, END_DATE_TIME, PROGRAMME_ID) values(1013,'2021-08-02T20:00:00Z','2021-08-02T23:00:00Z', 'PGM005');
