import os
import django

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'lab08.settings')
django.setup()

from datetime import date
from series.models import Serie

datos = [
    ("Friends", date(1994, 9, 22), 4, Serie.COMEDY),
    ("Narcos", date(2015, 8, 28), 4, Serie.ACTION),
    ("The Walking Dead", date(2010, 10, 31), 4, Serie.HORROR),
]
for nombre, fecha, rating, categoria in datos:
    s = Serie.objects.create(name=nombre, release_date=fecha, rating=rating, category=categoria)
    print("creada:", s.id, s.name)
