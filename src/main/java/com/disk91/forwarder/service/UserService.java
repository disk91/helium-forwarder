/*
 * Copyright (c) - Paul Pinault (aka disk91) - 2020.
 *
 *    Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 *    and associated documentation files (the "Software"), to deal in the Software without restriction,
 *    including without limitation the rights to use, copy, modify, merge, publish, distribute,
 *    sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 *    furnished to do so, subject to the following conditions:
 *
 *    The above copyright notice and this permission notice shall be included in all copies or
 *    substantial portions of the Software.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *    FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 *    OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *    WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 *    IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.disk91.forwarder.service;

import com.disk91.forwarder.ForwarderConfig;
import com.disk91.forwarder.jpa.db.HeliumUser;
import com.disk91.forwarder.jpa.repository.HeliumUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.Key;

@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected ForwarderConfig forwarderConfig;

    @Autowired
    protected UserCacheService userCacheService;

    @Autowired
    protected HeliumUserRepository heliumUserRepository;


    // ===================================================
    // Cache Management
    // ===================================================

    protected String bottomEmail_en;

    // ===================================================
    // Login Management
    // ===================================================

    /**
     * Extract the information from the chirpstack bearer
     * to be able to use them into the console bearer
     * do this without knowing the chirpstack signature key
     * @param bearer
     * @return
     */
    private Claims getUntrustedInfoFromBearer(String bearer) {
        // get the 2rd element of bearer xxxx.XXXXXX.xxxxx
        String [] split = bearer.split("\\.");
        if ( split.length >= 3 ) {
            String decoded = new String(Base64.decode(split[1]));
            ObjectMapper mapper = new ObjectMapper();
            try {
                Claims claims = mapper.readValue(decoded, Claims.class);
                return claims;
            } catch (JsonProcessingException x) {
                x.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Generate a uniq ket based on user key and server key to be able to deprecate any of these keys
     * @param hu
     * @return
     */
    public Key generateKeyForUser(HeliumUser hu) {
        String srvSecret = forwarderConfig.getJwtSignatureKey();
        String userSecret = hu.getUserSecret();
        byte [] secret = new byte[64];
        for ( int i = 0 ; i < 64 ; i++ ) {
            secret[i] = (byte) ((byte)(srvSecret.charAt(i)) ^ (2*(byte)(userSecret.charAt(i))));
        }
        return Keys.hmacShaKeyFor(secret);
    }


}
