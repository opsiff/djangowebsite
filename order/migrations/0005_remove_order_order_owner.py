# -*- coding: utf-8 -*-
# Generated by Django 1.11.20 on 2019-04-18 11:47
from __future__ import unicode_literals

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('order', '0004_order_order_owner'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='order',
            name='order_owner',
        ),
    ]