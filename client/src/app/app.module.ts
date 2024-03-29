import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'
import { HttpClientModule } from '@angular/common/http';
import { FlexLayoutModule } from '@angular/flex-layout';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { MainLayoutComponent } from './core/layouts/main-layout/main-layout.component';
import { AppRoutes } from './app-module.routing';
import { PageNotFoundComponent } from './core/page-not-found/page-not-found.component';
import { UserLayoutComponent } from './core/layouts/user-layout/user-layout.component';
import { MapCreatorLayoutComponent } from './core/layouts/map-creator-layout/map-creator-layout.component';
import { SharedModule } from './core/shared/shared.module';
import { GetLimitPoints } from './map-creator/getLimitPoints';

@NgModule({
  declarations: [
    AppComponent,
    MainLayoutComponent,
    PageNotFoundComponent,
    UserLayoutComponent,
    MapCreatorLayoutComponent,
  ],
  imports: [
    CommonModule,
    BrowserModule,
    RouterModule.forRoot(AppRoutes, { relativeLinkResolution: 'legacy' }),
    BrowserAnimationsModule,
    FlexLayoutModule,
    DragDropModule,
    HttpClientModule,
    SharedModule,
    MatMenuModule,
    MatButtonModule,
    MatInputModule,
    FormsModule
  ],
  providers: [GetLimitPoints],
  bootstrap: [AppComponent]
})
export class AppModule { }
