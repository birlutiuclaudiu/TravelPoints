INSERT INTO public.location(
    id, address, country, lat, "long")
VALUES ('f0ce067e-df92-11ed-b5ea-0242ac120002', 'Cluj-Napoca', 'Romania', 20.45, 45.34),
       ('067c7f8c-df93-11ed-b5ea-0242ac120002', 'Alba-Iulia', 'Romania', 25.45, 46.34),
       ('3b05bd18-df93-11ed-b5ea-0242ac120002', 'Sibiu', 'Romania', 21.45, 45.34),
       ('420f5c18-df93-11ed-b5ea-0242ac120002', 'Verona', 'Italia', 18.45, 43.34);

INSERT INTO public.touristic_attractions(
    id, audio_description, category, name, text_description, location)
VALUES ('82ece82c-df93-11ed-b5ea-0242ac120002', '...',  'CULTURAL', 'Moara de Vant',    'animalele sunt frumoase', 'f0ce067e-df92-11ed-b5ea-0242ac120002'),
       ('88595c1e-df93-11ed-b5ea-0242ac120002', '...',  'CULTURAL', 'Muzeul Unirii',    'in 1918 a fost realizat', '067c7f8c-df93-11ed-b5ea-0242ac120002'),
       ('8d9f495e-df93-11ed-b5ea-0242ac120002', '...',  'CULTURAL', 'Muzeul Bruckental', 'construit in 1901',      '3b05bd18-df93-11ed-b5ea-0242ac120002'),
       ('91a99d06-df93-11ed-b5ea-0242ac120002', '...',  'CULTURAL', 'Romeo si Jullieta', 'drama',                  '420f5c18-df93-11ed-b5ea-0242ac120002');
