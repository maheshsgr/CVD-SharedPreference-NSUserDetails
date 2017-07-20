//
//  lmsStorePlugIn.m
//  lmsmobile1
//
//  Created by Macintosh User on 25/1/14.
//  Copyright (c) 2014 __MyCompanyName__. All rights reserved.
//

#import "lmsStorePlugIn.h"


@implementation LmsStorePlugIn

- (CDVPlugin *)initWithWebView:(UIWebView *)theWebView
{
    self = [super init];
    if (self) {
        NSLog(@"plug in successfully initialized");
    }
    return self;
}

- (void)pluginInitialize
{
    NSLog(@"plug in getting re-initialized");
    [super pluginInitialize];
}

/*- (void) updateAccessToken:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options
{ 
    //get the callback id 
    lmsStore *lmsds  = [[lmsStore alloc] init];
    NSString *callbackId = [arguments pop]; 
    //NSLog(@"Hello, this is a native function called from PhoneGap/Cordova!"); 
    NSArray *resultArray = [arguments objectAtIndex:0];
    NSString *resultNature = [resultArray objectAtIndex:0]; 
    CDVPluginResult *result; 
    NSLog(@"result nature %@", resultNature);
    if ( [resultNature isEqualToString:@"success"]) 
    { 
        [lmsds writeAccessToken:[NSString stringWithFormat:@"%@", [resultArray objectAtIndex:1]]];
        [lmsds writeUserId:[NSString stringWithFormat:@"%@", [resultArray objectAtIndex:2]]];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString: @"Success :)"]; 
        [self writeJavascript:[result toSuccessCallbackString:callbackId]]; 
        return;
    }
} */

- (void) updateAccessToken:(CDVInvokedUrlCommand*)command;
{
    [self.commandDelegate runInBackground:^{
        //NSString *callbackId = command.callbackId; //  [arguments pop];
        NSArray *resultArray = [command.arguments objectAtIndex:0];
        NSLog(@"result array %@", resultArray);
      //  CDVPluginResult *result;
        [lmsStore writeAccessToken:[NSString stringWithFormat:@"%@", [command.arguments objectAtIndex:0]]];
        [lmsStore writeUserId:[NSString stringWithFormat:@"%@", [command.arguments objectAtIndex:1]]];
        [lmsStore writeDomainName:[NSString stringWithFormat:@"%@", [command.arguments objectAtIndex:2]]];
        
        return;
    }];
    //result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString: @"Success :)"];
    //[self writeJavascript:[result toSuccessCallbackString:callbackId]];
}

/*- (void) doLogOut:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options
{ 
    NSLog(@"logout cordova plug in invoked");
    lmsStore *lmsds = [[lmsStore alloc] init];
    [lmsds removeEntireLoginInformation];
    [[NSNotificationCenter defaultCenter] postNotificationName:@"exitAndReEnter" object:self]; 
}*/

- (void) doLogOut:(CDVInvokedUrlCommand*)command
{
    NSLog(@"logout cordova plug in invoked");
    [lmsStore removeEntireLoginInformation];
    //[[NSNotificationCenter defaultCenter] postNotificationName:@"exitAndReEnter" object:self];
    [defaults exitCallback](nil);
}

- (void) doMakeNSLog:(CDVInvokedUrlCommand*) command
{
    NSArray *resultArray = [command.arguments objectAtIndex:0];
    NSString *resultNature = [resultArray objectAtIndex:0];
    NSLog(@"cordova log is %@" , resultNature);
}
    
- (void) getAuthInformation:(CDVInvokedUrlCommand*) command
{
    NSLog(@"get auth information called in native");
        //[self.commandDelegate runInBackground:^{
        CDVPluginResult *result;
        NSString *callbackId = command.callbackId;
        NSMutableDictionary * l_authDict = [[NSMutableDictionary alloc] init];
        [l_authDict setValue:[lmsStore accessCode] forKey:@"accessCode"];
        
        NSString *l_accesstoken = [lmsStore accessToken];
        if (l_accesstoken == nil)
            [l_authDict setValue:@"accessCode" forKey:@"accessType"];
        else
            [l_authDict setValue:@"accessToken" forKey:@"accessType"];
        //retstring = [[NSString alloc] initWithFormat:@"%@,%@,%@", @"accesscode", l_accesscode,[lmsStore domainName]];
        [l_authDict setValue:[lmsStore accessToken] forKey:@"accessToken"];
        [l_authDict setValue:[lmsStore userId] forKey:@"accessUserID"];
        [l_authDict setValue:[lmsStore domainName] forKey:@"domainName"];
        [l_authDict setValue:[lmsStore userName] forKey:@"accessUserName"];
        [l_authDict setValue:[lmsStore apiToken] forKey:@"apiToken"];
        NSError * l_parseerror = nil;
        NSData * l_jsonData = [NSJSONSerialization dataWithJSONObject:l_authDict options:NSJSONWritingPrettyPrinted error:&l_parseerror];
        if (l_parseerror==nil)
        {
            NSString * l_jsAuthInfo =[NSString stringWithFormat:@"%@",[[NSString alloc] initWithData:l_jsonData encoding:NSUTF8StringEncoding]];
            NSLog(@"the set auth information %@", l_jsAuthInfo);
            // [self.commandDelegate evalJs:[[NSString alloc] initWithData:[l_jsAuthInfo dataUsingEncoding:NSUTF8StringEncoding] encoding:NSUTF8StringEncoding]];
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:l_jsAuthInfo];
            [self.commandDelegate sendPluginResult:result callbackId:callbackId];
        }
        //}];
}

@end
