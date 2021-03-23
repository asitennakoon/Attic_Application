/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { FurnitureViewComponent } from './furniture-view.component';

describe('FurnitureViewComponent', () => {
  let component: FurnitureViewComponent;
  let fixture: ComponentFixture<FurnitureViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FurnitureViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FurnitureViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
