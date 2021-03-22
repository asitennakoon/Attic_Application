/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { FirestoreService } from './firestore.service';

describe('Service: Firestore', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FirestoreService]
    });
  });

  it('should ...', inject([FirestoreService], (service: FirestoreService) => {
    expect(service).toBeTruthy();
  }));
});
