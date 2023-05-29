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

import com.disk91.forwarder.jpa.db.HeliumUser;
import com.disk91.forwarder.jpa.db.User;
import com.disk91.forwarder.jpa.repository.HeliumUserRepository;
import com.disk91.forwarder.jpa.repository.UserRepository;
import fr.ingeniousthings.tools.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

@Service
public class UserCacheService {

    public static final String HUPROFILE_STATUS_CREATED="created";
    public static final String HUPROFILE_STATUS_COMPLETED="completed";

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected HeliumUserRepository heliumUserRepository;


    // ===================================================
    // Cache Management
    // ===================================================
    public class UserCacheElement implements ClonnableObject<UserCacheElement> {
        public User user;
        public HeliumUser heliumUser;

        public UserCacheElement clone() {
            log.error("### UserCacheElement clone not implemented");
            return null;
        }
    }

    private ObjectCache<String, UserCacheElement> userCache;
    @PostConstruct
    private void initUserCacheService() {
        log.info("initUserCacheService initialization");
        this.userCache = new ObjectCache<String, UserCacheElement>("UserCache", 2000) {
            @Override
            public void onCacheRemoval(String key, UserCacheElement obj, boolean batch, boolean last) {
                // nothing to do, readOnly
            }

            @Override
            public void bulkCacheUpdate(List<UserCacheElement> objects) {

            }
        };
    }


    public UserCacheElement getUserByUsername(String userName) {
        User u = userRepository.findOneUserByEmail(userName);
        if ( u == null ) return null;
        return getUserById(u.getId().toString());
    }

    // get user if exist from cache, then from DB
    public UserCacheElement getUserById(String userId) {
        UserCacheElement r = this.userCache.get(userId);
        if ( r == null ) {
            // search in db
            User u = userRepository.findOneUserById(UUID.fromString(userId));
            if ( u == null ) return null;

            // search the heliumUser
            HeliumUser h = heliumUserRepository.findOneHeliumUserByUserid(u.getId().toString());
            if ( h == null ) {
                // happen on initialization ...
                h = createHeliumUserFromUser(u,"default");
            }

            r = new UserCacheElement();
            r.user = u;
            r.heliumUser = h;
            this.userCache.put(r,userId);
        }
        return r;
    }



    protected HeliumUser createHeliumUserFromUser(User u, String offer) {
        HeliumUser h = new HeliumUser();
        h.setUserid(u.getId().toString());
        h.setUsername(u.getEmail());
        h.setProfileStatus(HUPROFILE_STATUS_CREATED);
        h.setUserSecret(RandomString.getRandomAZString(64));
        h.setDefaultOffer(offer);
        h = heliumUserRepository.save(h);
        return h;
    }

}
