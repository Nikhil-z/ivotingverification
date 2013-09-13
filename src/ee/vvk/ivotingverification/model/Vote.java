/** * Copyright (C) 2013 Eesti Vabariigi Valimiskomisjon  * (Estonian National Electoral Committee), www.vvk.ee * * Written in 2013 by AS Finestmedia, www.finestmedia.ee *  * Vote-verification application for Estonian Internet voting system * * This program is free software: you can redistribute it and/or modify * it under the terms of the GNU General Public License as published by * the Free Software Foundation, either version 3 of the License, or * (at your option) any later version. * * This program is distributed in the hope that it will be useful, * but WITHOUT ANY WARRANTY; without even the implied warranty of * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License * along with this program.  If not, see <http://www.gnu.org/licenses/>. **/ package ee.vvk.ivotingverification.model;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import android.util.Log;import ee.vvk.ivotingverification.util.RegexMatcher;import ee.vvk.ivotingverification.util.Util;/** * Election, elector and vote information. *  * @version 16.05.2013 */public class Vote {	public int electionCount;	public String[] electionNames;	public String electionId = "";	public HashMap<String, String> encBallots;	public String electorName = "";	public String electorPersonalCode = "";	public String selectionList = "";	public void parseHeader(String in) {		getElectionNames(in.split("\n")[0]);		getEncBallots(in, electionCount);		getElectorData(in.split("\n")[electionCount + 2]);	}	private void getElectorData(String in) {		electorName = in.split("\t")[0];		if (RegexMatcher.isElevenDigets(in.split("\t")[1])) {			electorPersonalCode = in.split("\t")[1];		} else {			electorPersonalCode = "";		}	}	private void getEncBallots(String in, int count) {		String tempEncBallot = "";		encBallots = new HashMap<String, String>();		for (int i = 1; i <= count; i++) {			tempEncBallot = in.split("\n")[i];			encBallots.put(tempEncBallot.split("\t")[0],					tempEncBallot.split("\t")[1]);		}	}	private void getElectionNames(String in) {		String[] tempElectionNames = in.split("\t");		electionCount = tempElectionNames.length;		if (Util.DEBUGGABLE) {			Log.d("DEBUG", "Names count: " + electionCount);		}		electionNames = new String[electionCount + 1];		for (int i = 0; i < electionCount; i++) {			electionNames[i] = tempElectionNames[i].split(":")[0];		}	}	public List<Candidate> parseBody(String in) {		return readCandidates(in);	}	private List<Candidate> readCandidates(String in) {		List<Candidate> candidates = new ArrayList<Candidate>();		String[] candidatesList = in.split("\n");		for (int i = electionCount + 2; i < candidatesList.length; i++) {			candidates.add(readCandidate(candidatesList[i]));		}		return candidates;	}	/**	 * The information about an active candidate of the election.	 * 	 * @version 16.05.2013	 */	public static class Candidate {		public final String name;		public final String number;		public final String party;		private Candidate(String name, String number, String party) {			this.name = name;			this.number = number;			this.party = party;		}	}	private Candidate readCandidate(String candidate) {		String cName = null;		String cNumber = null;		String cParty = null;		cNumber = candidate.split("\t")[3];		cName = candidate.split("\t")[4];		if (candidate.split("\t").length > 5) {			cParty = candidate.split("\t")[5];		} else {			cParty = "";		}		return new Candidate(cName, cNumber, cParty);	}}