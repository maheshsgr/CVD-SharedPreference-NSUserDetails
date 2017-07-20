//
//  lmsStorePlugIn.h
//  lmsmobile1
//
//  Created by Macintosh User on 25/1/14.
//  Copyright (c) 2014 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Cordova/CDVPlugin.h>
#import <Cordova/CDV.h>
#import "lmsStore.h"
#import "defaults.h"

@interface LmsStorePlugIn : CDVPlugin 
{
    
}

/*- (void) updateAccessToken:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;
- (void) doLogOut:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options ;*/
- (void) updateAccessToken:(CDVInvokedUrlCommand*)command;
- (void) doLogOut:(CDVInvokedUrlCommand*)command;
- (void) doMakeNSLog:(CDVInvokedUrlCommand*) command;
- (void) getAuthInformation:(CDVInvokedUrlCommand*) command;
    
//(CDVInvokedUrlCommand*)command
@end