# Generated by Django 2.2.7 on 2019-11-28 13:51

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Message',
            fields=[
                ('msgId', models.CharField(max_length=30, primary_key=True, serialize=False)),
                ('linkman', models.CharField(max_length=20)),
                ('contactWay', models.CharField(max_length=30)),
                ('price', models.FloatField()),
                ('detail', models.CharField(max_length=240)),
                ('img', models.CharField(max_length=200)),
                ('post_date', models.DateTimeField(auto_now_add=True)),
                ('last_date', models.DateTimeField(auto_now=True)),
            ],
        ),
    ]
