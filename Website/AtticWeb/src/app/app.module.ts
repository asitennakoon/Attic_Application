import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule, routingComponents } from './app-routing.module';
import { AppComponent } from './app.component';

// importing firebase modules for database purposes.
import { AngularFireModule } from "@angular/fire";
import { AngularFirestoreModule } from "@angular/fire/firestore";

import { AngularFireStorageModule,} from "@angular/fire/storage";
// database configurations are declared in the environment
import { firebaseConfig } from 'src/environments/environment';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

// import forms module to handle forms
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// angular material imports
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatButtonModule} from '@angular/material/button';
import {MatInputModule} from '@angular/material/input';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatCardModule} from '@angular/material/card';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import { FileUploadComponent } from './components/file-upload/file-upload.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { HeaderComponent } from './components/header/header.component';
import { FurnitureViewComponent } from './components/furniture-view/furniture-view.component';


@NgModule({
  declarations: [
    AppComponent,
    routingComponents,
    FileUploadComponent,
    HeaderComponent,
    FurnitureViewComponent,
  
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,

    AngularFireModule.initializeApp(firebaseConfig),
    AngularFirestoreModule,
    AngularFireStorageModule,
    

    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,

    MatProgressBarModule,
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule,
    MatCheckboxModule,
    MatButtonModule,
    MatInputModule,
    MatAutocompleteModule,
    MatDatepickerModule,
    MatFormFieldModule,

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }


