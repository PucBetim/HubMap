import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'
import { FlexLayoutModule } from '@angular/flex-layout';
import {DragDropModule} from '@angular/cdk/drag-drop';

import { AppComponent } from './app.component';
import { MainLayoutComponent } from './core/layouts/main-layout/main-layout.component';
import { AppRoutes } from './app-module.routing';
import { LandingComponent } from './landing/landing.component';
import { LoginComponent } from './user/login/login.component';
import { PageNotFoundComponent } from './core/page-not-found/page-not-found.component';
import { UserLayoutComponent } from './core/layouts/user-layout/user-layout.component';
import { MapCreatorLayoutComponent } from './core/layouts/map-creator-layout/map-creator-layout.component';

@NgModule({
  declarations: [
    AppComponent,
    MainLayoutComponent,
    LandingComponent,
    LoginComponent,
    PageNotFoundComponent,
    UserLayoutComponent,
    MapCreatorLayoutComponent
  ],
  imports: [
    CommonModule,
    BrowserModule,
    RouterModule.forRoot(AppRoutes, { relativeLinkResolution: 'legacy' }),
    BrowserAnimationsModule,
    FlexLayoutModule,
    DragDropModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
